package dcc

import grails.plugins.springsecurity.Secured;
import org.springframework.dao.DataIntegrityViolationException

class JudgeController {
	def judgeService

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	/*
	 * Figure out who is signed in using built-in methods from the Spring Security Core plugin.
	 * Get a count of the total submissions in the database so far that are within the cutoff date.
	 * Figure out how many submissions the judge has not ranked, if any, to show him or her.
	 */
    @Secured(['ROLE_ADMIN','ROLE_JUDGE'])
	def index() {
		def result = judgeService.index()
		if(!result.error){
			return [submissionsText:result.sub,unrankedSubmissionsText:result.unsub]
		}
		flash.message = message(code: result.error.code,args:result.error.args)
		redirect(url:'/')
		return
    }

	/*
	 * Find all of the submissions the logged in judge has not ranked.
	 * Display them in a list view for him or her to rank.
	 */
	@Secured(['ROLE_JUDGE','ROLE_ADMIN'])
	def unrankedSubmissions() {
		def result = judgeService.unrankedSubmissions()
		if(!result.error){
			return [submissionInstanceList: result.submissionsList]
		}
		flash.message = message(code: result.error.code, args: result.error.args)
		redirect(action: 'index')
		return
	}

	/*
	 * A judge clicks on a submission to rank it.
	 * rankSubmission is called in judgeService.
	 * If the submission is found, it is returned.
	 * It is shown on a new page along with the judging form for the judge to rank the submission.
	 * If it is not found, the judge is taken back to the list of unranked submissions and notified that it could not be found
	 */
    @Secured(['ROLE_JUDGE','ROLE_ADMIN'])
	def rankSubmission(Long id) {
		def result = judgeService.rankSubmission(id)
		if(!result.error){
			return [submissionInstance: result.submissionInstance,rankInstance:result.rankInstance]
		}
		flash.message = message(code: result.error.code, args: result.error.args)
		redirect(action: "unrankedSubmissions")
		return
	}

	/*
	 * When the judge is ready to submit their rank, this method is called.
	 * Save the rank by filing up the fields from the values in the params list.
	 * Add a reference to the submission that was being ranked to the rankInstance
	 * Add a reference to the judge that did the ranking to the rankInstance.
	 */
	@Secured(['ROLE_JUDGE','ROLE_ADMIN'])
	def saveRank(){
		try{
			def result = judgeService.saveRank(params)
			if(!result.error){
				flash.message = message(code: 'default.created.message', args: [message(code: 'rank.label', default: 'Rank'), result.rankInstance.id])
				redirect(action: "unrankedSubmissions")
				return
			}
			flash.message = message(code: result.error.code, args: result.error.args)
			render(view: "rankSubmission", model: [submissionInstance:Submission.get(params.submissionId),rankInstance: new Rank(params)])
			return
		}
		catch(Exception e){
			flash.message = message(code: "default.method.failure", args: ["Rank could not be created.","Verify correct data is being used in the fields below."])
			render(view: "rankSubmission", model: [submissionInstance:Submission.get(params.submissionId),rankInstance: new Rank(params)])
			return
		}
	}

	/*
	 * Show an admin a list of the judges in the database.
	 * From here, the admin can click on one of the judges to update or remove him or her.
	 */
    @Secured(['ROLE_ADMIN'])
	def list(Integer max,Integer offset) {
		def result = judgeService.list(max,offset,params)
		if(result.judgeInstanceTotal == 0) {
			flash.message = "No Judges found.  Create a new judge."
			redirect(action:'create')
			return
		}
		else if(!result.error){
			return [judgeInstanceList: result.judgeInstanceList, judgeInstanceTotal: result.judgeInstanceTotal]
		}
		flash.message = message(code: result.error.code, args: result.error.args)
		redirect(controller:'admin',action:'index')
		return
	}

	/*
	 * Admins can create new judges.
	 * Creates a new judgeInstance and displays a form 
	 * for the admin to use to create the new judge.
	 */
    @Secured(['ROLE_ADMIN'])
	def create(){
		def result = judgeService.create(params)
		if(!result.error){
			return [judgeInstance: result.judgeInstance]
		}
		flash.message = message(code: result.error.code, args: result.error.args)
		redirect(action: 'list')
	}

	/*
	 * Save the judge using the values in the params list.
	 * If the ROLE_JUDGE role does not exist, create it in the SecRole table.
	 * Create an entry in the SecUserSecRole table that is a reference to the newly created judge and their role, ROLE_JUDGE.
	 * This role is used to secure certain pages/methods to only allow Judges who are logged in to view/use those pages/methods
	 */
    @Secured(['ROLE_ADMIN'])
	def save() {
		try{
			def result = judgeService.save(params)
			if (!result.error) {
				flash.message = "Judge $result.judgeInstance.username, has been created!"
				redirect(action: "show", id: result.judgeInstance?.id)
				return
			}
			flash.message = message(code: result.error.code, args: result.error.args)
			render(view: "create", model: [judgeInstance: result.judgeInstance])
			return
		}
		catch(Exception e){
			flash.message = message(code: "default.method.failure", args: ["Judge could not be created.","Verify correct data is being used in the fields below."])
			render(view:'create', model:[judgeInstance:new Judge(params)])
			return
		}
	}

	/*
	 * Show a specifically selected judge from the list that was generated from the list method.
	 */
    @Secured(['ROLE_ADMIN'])
	def show(Long id) {
		def result = judgeService.show(id)
		if (!result.error) {
			return [judgeInstance: result.judgeInstance]
		}
	   flash.message = message(code: result.error.code, args: result.error.args)
	   redirect(action: "list")
	   return
	}

	/*
	 * Edit the judge that was found and displayed in the show function.
	 */
	@Secured(['ROLE_ADMIN'])
	def edit(Long id) {
		def result = judgeService.edit(id)
		if (!result.error) {
			return [judgeInstance: result.judgeInstance]
		}
	   flash.message = message(code: result.error.code, args: result.error.args)
	   redirect(action: "list")
	   return
	}

	/*
	 * Change a judge's password
	 */
	@Secured(['ROLE_ADMIN'])
	def updatePassword(Long id) {
		def result = judgeService.updatePassword(id)
		if (!result.error) {
			return [judgeInstance: result.judgeInstance]
		}
	   flash.message = message(code: result.error.code, args: result.error.args)
	   redirect(action: "list")
	   return
	}

	/*
	 * Save the changes to a judge's record that were made using the edit method.
	 */
    @Secured(['ROLE_ADMIN'])
	def update(Long id, Long version) {
		try{
			def result = judgeService.update(id, version, params)
			if(!result.error){
				flash.message = message(code: 'default.updated.message', args: [message(code: 'judge.label', default: 'Judge'), params.id])
				redirect(action:"show", id: id)
				return
			}
			if(!result.judgeInstance) {
				flash.message = message(code: result.error.code, args: result.error.args)
				redirect(action: "list")
				return
			}
			flash.message = message(code: result.error.code, args: result.error.args)
			redirect(action: "edit", id: id)
			return
		}
		catch(Exception e){
			flash.message = message(code: "default.method.failure", args: ["Judge could not be updated.","Verify correct data is being used in the fields below."])
			redirect(action: "edit", id: id)
			return
		}
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
		try {
			def result = judgeService.delete(id)
			//Success
			if(!result.error){
				flash.message = message(code: 'default.deleted.message', args: [message(code: 'judge.label', default: 'Judge'), id])
				redirect(action: "list")
				return
			}
			//Could not find by id
			if(!result.judgeInstance) {
				flash.message = message(code: result.error.code, args: result.error.args)
				redirect(action: "list")
				return
			}
			//Found,could not delete
			flash.message = message(code: result.error.code, args: result.error.args)
			redirect(action: "show",id: id)
			return
		}
		catch(Exception e) {
			flash.message = message(code: "default.method.failure", args: ["Judge could not be deleted.",""])
			redirect(action: "show", id: id)
			return
		}
	}
}