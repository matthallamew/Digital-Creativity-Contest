package dcc

import org.springframework.transaction.annotation.Transactional

class AdminService {

	/*
	* Grab a count of how many submissions are in the database.
	* Display this on the Admin homepage
	*/
   def index(){
	   def result = [:]
	   def submissions = Submission.count()
	   def subs = (submissions <= 0) ? "Submissions are " : (submissions > 1) ? "Submissions are " : "Submission is "
	   result.sub = "$submissions $subs"
	   if(!result.sub){
		   result.error = [code:"default.method.failure",args:["Something went wrong counting the submissions."]]
	   }
	   return result
   }

	/*
	 * Get all the submissions, calculate the grandTotal for each submission
	 * Sort the submissions by category and then grandTotal
	 * Display a unique list of all the categories in the database
	 * Check for category list session variable so we don't have to keep querying the database to build the list
	 * When a user clicks on one of the categories, query the database for any submission that has that category
	 * Display the submissions found below the list of categories
	 *
	 */
	def showScoring(params,categoryList){
		def result = [:]
		if(params.category){
			params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
			params.offset = params.offset ? params.offset.toInteger() : 0
			result.categoryList = categoryList ?: Submission.withCriteria { projections{ distinct "category" }; ne "category",""; order "category";}
			result.submissionListTotal = Submission.countByCategory("${params.category}")
			result.submissionList = Submission.withCriteria{ maxResults params.max; firstResult params.offset; eq "category","${params.category}"; order "grandTotal","desc"; order "dateCreated","asc";}
			result.haveCategory = true
			result.category = params.category
			
			if(!result.submissionList || !result.submissionListTotal){
				result.error = [code:"default.method.failure",args:["Could not find any Submissions.",""]]
				return result
			}
		}
		else{
			categoryList = Submission.withCriteria {
				projections{ distinct "category" }
				ne "category",""
				order "category"
			}
			result.categoryList = categoryList
			if(!result.categoryList){
				result.error = [code:"default.method.failure",args:["Could not find any Submissions.",""]]
				return result
			}
		}
		return result
	}

	/*
	 * View an individual submission as an Admin
	 * Not functionally different than the Submission controller's show method
	 * Just makes it so the url is a little different, just to make it a little unique
	 */
	def showSubmission(Long id){
		def result = [:]
		result.submissionInstance = Submission.read(id)
		if (!result.submissionInstance) {
			result.error = [code: 'default.not.found.message', args: ["Submission", id]]
		}
		return result
	}

	/*
	 * Update the winner flag for an individual submission.
	 */
	@Transactional
	def updateWinnerStatus(params){
		 def result = [:]
		 result.submissionInstance = Submission.get(params?.id)
		 //Not Found
		 if(!result.submissionInstance) {
			 result.error = [code:'default.not.found.message', args: ['Submission', params?.id]]
			 return result
		 }
		 //Someone else updated it before we could
		 if(params?.version != null) {
			 if(result.submissionInstance.version > params?.version) {
				 result.error = [code:"default.optimistic.locking.failure",args:["Submission",""]]
				 return result
			 }
		 }
		 result.submissionInstance.winner = params.winner ?: false
		 //Could not save
		 if(result.submissionInstance.hasErrors() || !result.submissionInstance.save(flush: true)) {
			 result.error = [code:"default.method.failure",args:["Could not update Submission",""]]
			 return result
		 }
		 //Success
		 return result
	}

	/*
	 * Update grand total for each submission.
	 */
	@Transactional
	def updateGrandTotal(){
		def result = [:]
		def submissionList = Submission.list()
		if(!submissionList){
			result.error = [code:"default.method.failure",args:["Could not update grand total.",""]]
			return result
		}
		submissionList.each{sub ->
			if(!sub.updateGrandTotal()){
				throw new Exception()
			}
		}
		result.success = true
		return result
	}

	/*
	 * Show a list of Admin's in the database
	 */
	def list(Integer max,Integer offset,params) {
		def result = [:]
		params.max = Math.min(max ?: 10, 100)
		params.offset = offset ?: 0
		def role = SecRole.findByAuthority("ROLE_ADMIN")
		def susr = SecUserSecRole.findAllBySecRole(role, [max: params.max,offset: params.offset])
		def susrCount = SecUserSecRole.countBySecRole(role)
		result.secUser = susr.secUser
		result.secUserCount = susrCount
		if(result.secUserCount == 0) {
			return result
		}
		else if(!result.secUser || !result.secUserCount){
			result.error = [code:"default.method.failure",args:["Could not produce a list of Administrators.",""]]
			return result
		}
		return result
	}

	/*
	 * Create a new, blank secUserInstance to create an admin.
	 */
	@Transactional
	def create(params) {
		def result = [:]
		result.secUserInstance = new SecUser(params)
		if(!result.secUserInstance){
			result.error = [code:"default.method.failure",args:["Could not create a new Administrator.",""]]
		} 
		return result
	}

	/*
	 * Create a new secUserInstance and fill up the values from the
	 * params list which is sent over by posting the form on the create page.
	 * Create an entry in the SecUserSecRole table for user permissions.
	 */
	@Transactional
	def save(params) {
		def result = [:]
		result.secUserInstance = new SecUser(params)
		if(result.secUserInstance.hasErrors() || !result.secUserInstance.save(flush: true)){
			result.error = [code:"default.method.failure",args:["Could not create new Administrator",""]]
			return result
		}
		def adminRole = SecRole.findByAuthority("ROLE_ADMIN") ?: new SecRole(authority:"ROLE_ADMIN").save(flush:true)
		if(!SecUserSecRole.create(result.secUserInstance,adminRole)){
			result.error = [code:"default.method.failure",args:["Could not create new Administrator",""]]
			return result
		}
		return result
	}
	/*
	 * Show an individual admin to show extra properties of the admin, edit the admin, or delete the admin
	 */
	def show(Long id) {
		def result = [:]
		result.secUserInstance = SecUser.read(id)
		if (!result.secUserInstance) {
			result.error = [code:'default.not.found.message',args:['Admin',id]]
		}
		return result
	}

	/*
	 * Edit an individual admin to show extra properties of the admin, edit the admin, or delete the admin
	 */
	def edit(Long id) {
		def result = [:]
		result.secUserInstance = SecUser.get(id)
		if (!result.secUserInstance) {
			result.error = [code:'default.not.found.message',args:['Admin',id]]
		}
		return result
	}

	/*
	 * Save any changes made to the admin from the edit method
	 */
	@Transactional
	def update(Long id, Long version, params) {
		def result = [:]
		result.secUserInstance = SecUser.get(id)
		//Not Found
		if(!result.secUserInstance) {
			result.error = [code:'default.not.found.message', args: ['Admin', id]]
			return result
		}
		//Someone else updated it before we could
		if(version != null) {
			if(result.secUserInstance.version > version) {
				result.error = [code:"default.optimistic.locking.failure",args:["User",""]]
				return result
			}
		}
		//Could not save
		result.secUserInstance.properties = params
		if(result.secUserInstance.hasErrors() || !result.secUserInstance.save(flush: true)) {
			result.error = [code:"default.method.failure",args:["Could not update User",""]]
			return result
		}
		//Success
		return result
	}
	
	/*
	 * Find the admin in the SecUser table.
	 * Find the adminRole.
	 * Remove the record from the SecUserSecRole mapping table, then remove the admin from the SecUser table.
	 * It must be done in this order due to a foreign key restraint being enforced.  Deleting the admin first will fail.
	 * If there is no adminRole, remove any record related to that user from the SecUserSecRole table, regardless of the role(s) the record has.
	 */
	@Transactional
	def delete(Long id) {
		def result = [:]
		result.secUserInstance = SecUser.get(id)
		if(!result.secUserInstance) {
			result.error = [code: 'default.not.found.message', args: ['Admin', id]]
			return result
		}
		def adminRole = SecRole.findByAuthority("ROLE_ADMIN")
		if(adminRole){
			SecUserSecRole.remove result.secUserInstance, adminRole
		}
		else{
			SecUserSecRole.removeAll result.secUserInstance
		}
		result.secUserInstance.delete(flush: true)
		return result
	}
}