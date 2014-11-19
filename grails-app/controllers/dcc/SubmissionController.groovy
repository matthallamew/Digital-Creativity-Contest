package dcc

import grails.plugins.springsecurity.Secured;
import org.springframework.dao.DataIntegrityViolationException

class SubmissionController {
	
	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	
	def springSecurityService
	def submissionService

	/*
	 * Redirect to a list of the submissions
	 */
	@Secured(['ROLE_ADMIN'])
    def index() {
        redirect(action: "list", params: params)
    }

	/*
	 * Call list in submissionService.
	 * If there are no errors, return the list of submissions.
	 * If there are errors, redirect the user to the home page.
	 */
	@Secured(['ROLE_ADMIN','ROLE_JUDGE'])
    def list(Integer max) {
		def result = submissionService.list(max,params)
		if(!result.error){
			return [submissionInstanceList: result.submissionInstanceList, submissionInstanceTotal: result.submissionInstanceTotal]
		}
		flash.message = message(code: result.error.code, args: result.error.args)
		redirect(url: "/")      
    }

	/*
	 * Call listWinners in submissionService
	 * Show list of the winning submissions for the current year.
	 */
    def listWinners(Integer max) {
		def result = submissionService.listWinners(max,params)
		if(!result.error){
			return [submissionInstanceList: result.submissionInstanceList, submissionInstanceTotal: result.submissionInstanceTotal]
		}
		flash.message = message(code: result.error.code, args: result.error.args)
		redirect(url: "/")      
    }

	/*
	 * Call create in submissionService.
	 * If there are no errors, return a new submissionInstance.
	 * If there are errors, redirect to list.
	 */
    def create() {
		def result = submissionService.create(params)
		if(!result.error){
			return [submissionInstance: result.submissionInstance,submissionCategories:result.submissionCategories,submitDisabled:result.submitDisabled]
		}
		flash.message = message(code: result.error.code, args: result.error.args)
		redirect(action: 'list')
    }

	/*
	 * Call save in submissionService.
	 * If there are no errors, redirect to confirmSubmission to show the user it has been saved.
	 * If there are errors or an Exception is thrown,
	 * redirect back to the create stage with whatever values they had previously entered.
	 */
    def save() {
		try{
			def result = submissionService.save(params)
			if (!result.error) {
				flash.message = "Submission $result.submissionInstance.title, has been created!"
				redirect(action: "confirmSubmission", id: result.submissionInstance.id)
				return
			}
			flash.message = message(code: result.error.code, args: result.error.args)
			render(view: "create", model: [submissionInstance: result.submissionInstance])
			return
		}
		catch(Exception e){
			flash.message = message(code: "default.method.failure", args: ["Submission could not be created.","Verify correct data is being used in the fields below."])
			render(view:'create', model:[submissionInstance:new Submission(params)])
			return
		}		
    }

	/*
	 * Call confirmSubmission in submissionService.
	 * If there are no errors, return the submission that was found.
	 * If there were errors, redirect to the create stage. 
	 */
	def confirmSubmission(Long id) {
		def result = submissionService.confirmSubmission(id)
		if (!result.error) {
			return [submissionInstance: result.submissionInstance]
		}
	   flash.message = message(code: result.error.code, args: result.error.args)
	   redirect(action: "create",model:[submissionInstance:result.submissionInstance])
	   return
	}

	/*
	 * Call show in submissionService.
	 * If there are no errors, return the submission that was found.
	 * If there were errors, redirect to the list stage. 
	 */
	@Secured(['ROLE_JUDGE','ROLE_ADMIN'])
	def show(Long id) {
		def result = submissionService.show(id)
		if (!result.error) {
			return [submissionInstance: result.submissionInstance]
		}
	   flash.message = message(code: result.error.code, args: result.error.args)
	   redirect(action: "list")
	   return
	}

	/*
	 * Call showWinner in submissionService
	 * Show a specific winning submission.
	 */
	def showWinner(Long id) {
		def result = submissionService.showWinner(id)
		if (!result.error) {
			return [submissionInstance: result.submissionInstance]
		}
	   flash.message = message(code: result.error.code, args: result.error.args)
	   redirect(action: "listWinners")
	   return
	}
	
	/*
	 * Call edit in submissionService.
	 * If there are no errors, return the submission that was found.
	 * If there were errors, redirect to the list stage. 
	 */
	@Secured(['ROLE_ADMIN'])
	def edit(Long id) {
		def result = submissionService.edit(id)
		if (!result.error) {
			return [submissionInstance: result.submissionInstance]
		}
	   flash.message = message(code: result.error.code, args: result.error.args)
	   redirect(action: "list")
	   return
	}

	/*
	 * Call update in submissionService.
	 * If there are no errors, redirect to the show stage to display the submission that was just updated.
	 * If there are errors, redirect to the list stage.
	 */
	@Secured(['ROLE_ADMIN'])
	def update(Long id, Long version) {
		try{
			def result = submissionService.update(id, version, params)
			//Success
			if(!result.error){
				flash.message = message(code: 'default.updated.message', args: [message(code: 'submission.label', default: 'Submission'), params.id])
				redirect(action:"show", id: id)
				return
			}
			//Could not find by ID
			if(!result.submissionInstance) {
				flash.message = message(code: result.error.code, args: result.error.args)
				redirect(action: "list")
				return
			}
			//Could not update
			flash.message = message(code: result.error.code, args: result.error.args)
			redirect(action: "edit", id: id)
			return
		}
		catch(Exception e) {
			flash.message = message(code: "default.method.failure", args: ["Submission could not be updated.","Verify correct data is being used in the fields below."])
			redirect(action: "edit", id: id)
			return
		}
	}

	@Secured(['ROLE_ADMIN'])
    def delete(Long id) {
		try {
			def result = submissionService.delete(id)
			//Success
			if(!result.error){
				flash.message = message(code: 'default.deleted.message', args: [message(code: 'submission.label', default: 'Submission'), id])
				redirect(action: "list")
				return
			}
			//Could not find by ID
			if(!result.submissionInstance) {
				flash.message = message(code: result.error.code, args: result.error.args)
				redirect(action: "list")
				return
			}
			//Found, could not delete
			flash.message = message(code: result.error.code, args: result.error.args)
			redirect(action: "show",id: id)
			return
		}
		catch(Exception e) {
			flash.message = message(code: "default.method.failure", args: ["Submission could not be deleted.",""])			
			redirect(action: "show", id: id)
			return
		}
    }
}