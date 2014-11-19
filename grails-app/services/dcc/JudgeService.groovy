package dcc

import grails.validation.ValidationException
import org.springframework.transaction.annotation.Transactional

class JudgeService {
	
	def springSecurityService
	def grailsApplication
	
	/*
	* Grab a count of how many submissions are in the database.
	* Display this on the Judges home page
	*/
   def index(){
	   def result = [:]
	   def cutOffDate = Date.parse("MM/dd/yyyy hh:mm:ss","${AppConfig?.findByConfigKey('cutOffDate')?.configValue ?: '01/01/1980 23:59:59'}")
	   def submissions = Submission.countByDateCreatedLessThanEquals(cutOffDate)
	   result.sub = (submissions <= 0) ? "There are $submissions Submissions total." : (submissions > 1) ? "There are $submissions Submissions total." : "There is $submissions Submission total."
	   //Find all submissions that the currently logged in judge has not ranked.
	   def unrankedSubs = Submission.executeQuery("SELECT s FROM Submission s WHERE s.dateCreated <= ? AND s NOT IN(SELECT r.submissions FROM Rank r WHERE r.judges = ?)",[cutOffDate,user])
	   def unrankedSubmission = unrankedSubs.size()
	   result.unsub = (unrankedSubmission <= 0) ? "You have not ranked $unrankedSubmission Submissions." : (submissions > 1) ? "You have not ranked $unrankedSubmission Submissions." : "You have not ranked $unrankedSubmission Submission."
	   if(!result.sub || !result.unsub){
		   result.error = [code:'default.method.failure',args:["Could not show the Judges home page.",""]]
	   }
	   return result
   }

   def unrankedSubmissions() {
	   def result = [:]
	   def cutOffDate = Date.parse("MM/dd/yyyy hh:mm:ss","${AppConfig?.findByConfigKey('cutOffDate')?.configValue ?: '01/01/1980 23:59:59'}")
	   //find all submissions that the currently logged in judge has not ranked
	   result.submissionsList = Submission.executeQuery("SELECT s FROM Submission s WHERE s.dateCreated <= ? AND s NOT IN(SELECT r.submissions FROM Rank r WHERE r.judges = ?)",[cutOffDate,user])
	   if(!result){
		   result.error = [code: 'default.method.failure',args: ['Could not find the submissions that you need to rank.','']]
	   }
	   return result
   }

   @Transactional
   def rankSubmission(Long id) {
	   def result = [:]
	   result.rankInstance = new Rank()
	   result.submissionInstance = Submission.read(id)
	   if (!result.submissionInstance || !result.rankInstance) {
		   result.error = [code: 'default.not.found.message', args: ['Submission', id]]
	   }
	   return result
   }

   @Transactional
   def saveRank(params){
	   def result = [:]
	   result.rankInstance = new Rank(params)
	   result.rankInstance.submissions = Submission.load(params.submissionId)
	   if(user){
		   result.rankInstance.judges = user
	   }
	   if (!result.rankInstance.save(flush:true)) {
		   result.error = [code:'default.method.failure', args:['Could not save your rank for this submission.','']]
		   return result
	   }
	   if(!result.rankInstance.submissions.updateGrandTotal()){
		   result.error = [code:"default.method.failure",args:["Could not update grand total.",""]]
		   return result
	   }
	   return result
   }

   /*
	 * List the judges in the database.
	 */
	def list(Integer max,Integer offset,params) {
		def result = [:]
		params.max = Math.min(max ?: 10, 100)
		params.offset = offset ?: 0
		result.judgeInstanceList = Judge.list(params)
		result.judgeInstanceTotal = Judge.count()
		if(result.judgeInstanceTotal == 0) {
			return result
		}
		else if(!result.judgeInstanceList || !result.judgeInstanceTotal){
			result.error = [code:"default.method.failure",args:["Could not produce a list of judges.",""]]
			return result
		}
		return result
	}

	/*
	 * Create a new, blank judgeInstance to create a judge.
	 */
	@Transactional
	def create(params) {
		def result = [:]
		result.judgeInstance = new Judge(params)
		if(!result.judgeInstance){
			result.error = [code:"default.method.failure",args:["Could not create a new Judge.",""]]
		}
		return result
	}

	/*
	 * Create a new judgeInstance and fill up the values from the
	 * params list which is sent over by posting the form on the create page.
	 * Create an entry in the SecUserSecRole table for user permissions.
	 */
	@Transactional
	def save(params) {
		def result = [:]
		result.judgeInstance = new Judge(params)
		if(result.judgeInstance.hasErrors() || !result.judgeInstance.save(flush: true)){
			result.error = [code:"default.method.failure",args:["Could not create new Judge",""]]
			return result
		}
		def judgeRole = SecRole.findByAuthority("ROLE_JUDGE") ?: new SecRole(authority:"ROLE_JUDGE").save(flush:true)
		if(!SecUserSecRole.create(result.judgeInstance,judgeRole)){
			result.error = [code:'default.method.failure',args:["Could not create new Judge",""]]
			return result
		}
		return result
	}

	/*
	 * Show an individual judge.
	 */
	def show(Long id) {
		def result = [:]
		result.judgeInstance = Judge.read(id)
		if (!result.judgeInstance) {
			result.error = [code:'default.not.found.message',args:['Judge',id]]
		}
		return result
	}

	/*
	 * Edit an individual judge to show extra properties of the judge, edit the judge, or delete the judge
	 */
	def edit(Long id) {
		def result = [:]
		result.judgeInstance = Judge.get(id)
		if (!result.judgeInstance) {
			result.error = [code:'default.not.found.message',args:['Judge',id]]
		}
		return result
	}

	/*
	 * Edit an individual judge to show extra properties of the judge, edit the judge, or delete the judge
	 */
	def updatePassword(Long id) {
		def result = [:]
		result.judgeInstance = Judge.get(id)
		if (!result.judgeInstance) {
			result.error = [code:'default.not.found.message',args:['Judge',id]]
		}
		return result
	}

	/*
	 * Save any changes made to the judge from the edit method
	 */
	@Transactional
	def update(Long id, Long version, params) {
		def result = [:]
		result.judgeInstance = Judge.get(id)
		//Not Found
		if(!result.judgeInstance) {
			result.error = [code:'default.not.found.message', args: ['Judge', id]]
			return result
		}
		//Someone else updated it before we could
		if(version != null) {
			if(result.judgeInstance.version > version) {
				result.error = [code:"default.optimistic.locking.failure",args:["Judge",""]]
				return result
			}
		}
		//Could not save
		result.judgeInstance.properties = params
		if(result.judgeInstance.hasErrors() || !result.judgeInstance.save(flush: true)) {
			result.error = [code:"default.method.failure",args:["Could not update Judge",""]]
			return result
		}
		//Success
		return result
	}
	
	/*
	 * Try to find the judge.
	 * If it is not found, return an error.
	 * If it is found, try to delete it.
	 * If it cannot be deleted, return an error.
	 * If the judge had any ranks, update the grand_total of each submission after deleting the judge.
	 * If all goes well, return the good news.
	 */
	@Transactional
	def delete(Long id) {
		def result = [:]
		result.judgeInstance = SecUser.read(id)
		if(!result.judgeInstance) {
			result.error = [code: 'default.not.found.message', args: ['Judge', id]]
			return result
		}
		def judgeRole = SecRole.findByAuthority("ROLE_JUDGE")
		if(judgeRole){
			SecUserSecRole.remove result.judgeInstance, judgeRole
		}
		else{
			SecUserSecRole.removeAll result.judgeInstance
		}
		def rankCount = result.judgeInstance.ranks.size()
		result.judgeInstance.delete(flush: true)
		if(rankCount){
			if(!Submission.updateGrandTotalAll()){
				result.error = [code:"default.method.failure",args:["Could not update grand_total after deleting the judge.",""]]
				return result
			}
		}
		result.success = true
		return result
	}

/*
 * ***********************************************************************************************************************************************************
 */
	/* Note for the getUser method
	 * If you don't create a custom UserDetailsService, you will have to look up the user by username which is less efficient.
	 * You must do this because it won't associate the id with the user that is authenticated if you use LDAP or something other than base authentication.
	 * Since this application will have very few people logging in, efficiency should not be an issue.
	 */
	/*
	 * Return a reference to the SecUser that is logged in
	 */
	private getUser(){
		return SecUser.findByUsername(springSecurityService.principal.username)
	}
}