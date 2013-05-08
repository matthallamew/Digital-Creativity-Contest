package dcc

import grails.plugins.springsecurity.Secured;
import org.springframework.dao.DataIntegrityViolationException

class JudgeController {

	def springSecurityService
	def grailsApplication
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	/*
	 * Figure out who is signed in using built-in methods from the Spring Security Core plugin.
	 * Get a count of the total submissions in the database so far that are within the cutoff date.
	 * Figure out how many submissions the judge has not ranked, if any, to show him or her.
	 */
    @Secured(['ROLE_ADMIN','ROLE_JUDGE'])
	def index() {
		def user = getUser()
		def cutOffDate = grailsApplication.config.cutOffDate
		def submissions = Submission.countByDateCreatedLessThanEquals(cutOffDate)
		def sub = (submissions <= 0) ? "There are $submissions Submissions total." : (submissions > 1) ? "There are $submissions Submissions total." : "There is $submissions Submission total."
		//Find all submissions that the currently logged in judge has not ranked.
		//THANK YOU ALLISON JANSEN!  I could not figure out this simple query; I had thought about it too long/hard.
		def unrankedSubs = Submission.executeQuery("SELECT s FROM Submission s WHERE s.dateCreated <= ? AND s NOT IN(SELECT r.submissions FROM Rank r WHERE r.judges = ?)",[cutOffDate,user])		
		def unrankedSubmission = unrankedSubs.size()
		def unsub = (unrankedSubmission <= 0) ? "You have not ranked $unrankedSubmission Submissions." : (submissions > 1) ? "You have not ranked $unrankedSubmission Submissions." : "You have not ranked $unrankedSubmission Submission."
		[submissionsText:sub,unrankedSubmissionsText:unsub]
    }

	/*
	 * Find all of the submissions the logged in judge has not ranked.
	 * Display them in a list view for him or her to rank.
	 */
	@Secured(['ROLE_JUDGE','ROLE_ADMIN'])
	def unrankedSubmissions(Integer max) {
		def user = getUser()
		def cutOffDate = grailsApplication.config.cutOffDate
		//find all submissions that the currently logged in judge has not ranked
		def submissionsList = Submission.executeQuery("SELECT s FROM Submission s WHERE s.dateCreated <= ? AND s NOT IN(SELECT r.submissions FROM Rank r WHERE r.judges = ?)",[cutOffDate,user])

		params.max = Math.min(max ?: 10, 100)
		[submissionInstanceList: submissionsList]
	}

	/*
	 * A judge clicks on a submission to rank it.
	 * If the submission is found, it is returned.
	 * It is shown on a new page along with the judging form for the judge to rank the submission.
	 * If it is not found, the judge is taken back to the list of unranked submissions and notified that it could not be found
	 */
    @Secured(['ROLE_JUDGE','ROLE_ADMIN'])
	def rankSubmission(Long id) {
		def rankInstance = new Rank()
		def submissionInstance = Submission.get(id)
		if (!submissionInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'submission.label', default: 'Submission'), id])
			redirect(action: "unrankedSubmissions")
			return
		}
		[submissionInstance: submissionInstance,rankInstance:rankInstance]
	}

	/*
	 * When the judge is ready to submit their rank, this method is called.
	 * Save the rank by filing up the fields from the values in the params list.
	 * Add a reference to the submission that was being ranked to the rankInstance
	 * Add a reference to the judge that did the ranking to the rankInstance.
	 */
    @Secured(['ROLE_JUDGE','ROLE_ADMIN'])
	def saveRank(){
		def user = getUser()
		def rankInstance = new Rank(params)
		def submission = Submission.get(params.submissionId)
		rankInstance.submissions = submission
		if(user){
			rankInstance.judges = user
		}
		
		if (!rankInstance.save(flush:true)) {
			flash.message = "Woops, couldn't save."
			render(view: "rankSubmission", model: [submissionInstance:submission,rankInstance: rankInstance])
			return
		}

		flash.message = message(code: 'default.created.message', args: [message(code: 'rank.label', default: 'Rank'), rankInstance.id])
		redirect(controller:"judge",action: "unrankedSubmissions")
	}

	/*
	 * Show an admin a list of the judges in the database.
	 * From here, the admin can click on one of the judges to update or remove him or her.
	 */
    @Secured(['ROLE_ADMIN'])
	def list(Integer max,Integer offset) {
		params.max = Math.min(max ?: 10, 100)
		params.offset = Math.min(offset ?: 0, 100)
		def role = SecRole.findByAuthority("ROLE_JUDGE")
		def susr = SecUserSecRole.findAllBySecRole(role, [max: params.max ?: 10,offset: params.offset ?: 0])
		def susrCount = SecUserSecRole.countBySecRole(role)
		[judgeInstanceList: susr.secUser, judgeInstanceTotal: susrCount]
	}

	/*
	 * Admins can create new judges.
	 * Creates a new judgeInstance and displays a form 
	 * for the admin to use to create the new judge.
	 */
    @Secured(['ROLE_ADMIN'])
    def create() {
        [judgeInstance: new Judge(params)]
    }

	/*
	 * Save the judge using the values in the params list.
	 * If the ROLE_JUDGE role does not exist, create it in the SecRole table.
	 * Create an entry in the SecUserSecRole table that is a reference to the newly created judge and their role, ROLE_JUDGE.
	 * This role is used to secure certain pages/methods to only allow Judges who are logged in to view/use those pages/methods
	 */
    @Secured(['ROLE_ADMIN'])
    def save() {
        def judgeInstance = new Judge(params)
		judgeInstance.enabled = true
        if (!judgeInstance.save(flush: true)) {
            render(view: "create", model: [judgeInstance: judgeInstance])
            return
        }
		def judgeRole = SecRole.findByAuthority("ROLE_JUDGE") ?: new SecRole(authority:"ROLE_JUDGE").save(flush:true)
		SecUserSecRole.create judgeInstance,judgeRole

        flash.message = message(code: 'default.created.message', args: [message(code: 'judge.label', default: 'Judge'), judgeInstance.id])
        redirect(action: "show", id: judgeInstance.id)
    }

	/*
	 * Show a specifically selected judge from the list that was generated from the list method.
	 */
    @Secured(['ROLE_ADMIN'])
    def show(Long id) {
        def judgeInstance = Judge.get(id)
        if (!judgeInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'judge.label', default: 'Judge'), id])
            redirect(action: "list")
            return
        }

        [judgeInstance: judgeInstance]
    }

	/*
	 * Edit the judge that was found and displayed in the show function.
	 */
    @Secured(['ROLE_ADMIN'])
    def edit(Long id) {
        def judgeInstance = Judge.get(id)
        if (!judgeInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'judge.label', default: 'Judge'), id])
            redirect(action: "list")
            return
        }

        [judgeInstance: judgeInstance]
    }

	/*
	 * Save the changes to a judge's record that were made using the edit method.
	 */
    @Secured(['ROLE_ADMIN'])
    def update(Long id, Long version) {
        def judgeInstance = Judge.get(id)
        if (!judgeInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'judge.label', default: 'Judge'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (judgeInstance.version > version) {
                judgeInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'judge.label', default: 'Judge')] as Object[],
                          "Another user has updated this Judge while you were editing")
                render(view: "edit", model: [judgeInstance: judgeInstance])
                return
            }
        }

        judgeInstance.properties = params

        if (!judgeInstance.save(flush: true)) {
            render(view: "edit", model: [judgeInstance: judgeInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'judge.label', default: 'Judge'), judgeInstance.id])
        redirect(action: "show", id: judgeInstance.id)
    }

	/*
	 * Delete the judge that was found and displayed in the show function.
	 * Get a reference to the judgeRole.
	 * Remove the record from the SecUserSecRole mapping table, then remove the judge from the SecUser table.
	 * It must be done in this order due to a foreign key restraint being enforced.  Deleting the judge first will fail.
	 * If there is no judgeRole, remove any record from the SecUserSecRole table, regardless of the role(s) the record has.
	 */
    @Secured(['ROLE_ADMIN'])
    def delete(Long id) {
        def judgeInstance = Judge.get(id)
        if (!judgeInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'judge.label', default: 'Judge'), id])
            redirect(action: "list")
            return
        }

        try {
			def judgeRole = SecRole.findByAuthority("ROLE_JUDGE")
			if(judgeRole){
				SecUserSecRole.remove judgeInstance,judgeRole
				judgeInstance.delete(flush: true)
				flash.message = message(code: 'default.deleted.message', args: [message(code: 'judge.label', default: 'Judge'), id])
				redirect(action: "list")
			}
			else{
				SecUserSecRole.removeAll judgeInstance
				judgeInstance.delete(flush: true)
				flash.message = message(code: 'default.deleted.message', args: [message(code: 'judge.label', default: 'Judge'), id])
				redirect(action: "list")
			}
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'judge.label', default: 'Judge'), id])
            redirect(action: "show", id: id)
        }
    }

	
	/* For the getUser method
	 * If you don't create a custom UserDetailsService, you will have to look up the user by username which is less efficient.
	 * You must do this because it won't associate the id with the user that is authenticated if you use LDAP or something other than base authentication.
	 * Since this application will have very few people logging in, efficiency should not be an issue.
	 */
	
	/*
	 * Return a reference to the SecUser that is logged in
	 */
	private getUser(){
		def user = SecUser.findByUsername(springSecurityService.principal.username)
		return user
	}
}