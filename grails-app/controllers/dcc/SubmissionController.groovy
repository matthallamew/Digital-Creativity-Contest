package dcc

import grails.plugins.springsecurity.Secured;
import org.springframework.dao.DataIntegrityViolationException

class SubmissionController {
	
	def springSecurityService
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	/*
	 * Redirect to a list of the submissions
	 */
	@Secured(['ROLE_ADMIN'])
    def index() {
        redirect(action: "list", params: params)
    }

	/*
	 * List all of the submissions in the database
	 */
	@Secured(['ROLE_ADMIN','ROLE_JUDGE'])
    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [submissionInstanceList: Submission.list(params), submissionInstanceTotal: Submission.count()]
    }

	/*
	 * Create a new, blank submissionInstance
	 * Preload it with some values if the user has recently entered a submission (and if the options are uncommented in the method)
	 */
    def create() {
//	Preset some values for the new submission if they were set from a previous new submission
//		if(session.category){
//			params.category = session.category
//		}
//		if(session.author){
//			params.author = session.author
//		}
//		if(session.emailAddr){
//			params.emailAddr = session.emailAddr
//		}
        [submissionInstance: new Submission(params)]
    }

	/*
	 * Create a new submissionInstance and fill up the values from the
	 * params list which are sent over by posting the form on the create page.
	 * Set session variables to preset values the next time a user creates a new submission (if uncommented below)
	 */
    def save() {
        def submissionInstance = new Submission(params)
//	Save some values to the current session to help users fill out new submissions faster
//		session['category'] = params.category
//		session['author'] = params.author
//		session['emailAddr'] = params.emailAddr
        if (!submissionInstance.save(flush: true)) {
            render(view: "create", model: [submissionInstance: submissionInstance])
            return
        }
//        flash.message = message(code: 'default.created.message', args: [message(code: 'submission.label', default: 'Submission'), submissionInstance.id])
		flash.message = "Your submission, $submissionInstance.title, has been created!"
        redirect(action: "confirmSubmission", id: submissionInstance.id)
    }

	/*
	 * Show the user that their submission has been created.
	 * Don't use the default show method, that is for logged in users.
	 */
	def confirmSubmission(Long id) {
		def submissionInstance = Submission.get(id)
		if (!submissionInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'submission.label', default: 'Submission'), id])
			redirect(action: "create")
			return
		}
		[submissionInstance: submissionInstance]
	}

	@Secured(['ROLE_JUDGE','ROLE_ADMIN'])
    def show(Long id) {
        def submissionInstance = Submission.get(id)
        if (!submissionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'submission.label', default: 'Submission'), id])
            redirect(action: "list")
            return
        }
        [submissionInstance: submissionInstance]
    }

	@Secured(['ROLE_ADMIN'])
    def edit(Long id) {
        def submissionInstance = Submission.get(id)
        if (!submissionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'submission.label', default: 'Submission'), id])
            redirect(action: "list")
            return
        }
        [submissionInstance: submissionInstance]
    }

	@Secured(['ROLE_ADMIN'])
    def update(Long id, Long version) {
        def submissionInstance = Submission.get(id)
        if (!submissionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'submission.label', default: 'Submission'), id])
            redirect(action: "list")
            return
        }
        if (version != null) {
            if (submissionInstance.version > version) {
                submissionInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'submission.label', default: 'Submission')] as Object[],
                          "Another user has updated this Submission while you were editing")
                render(view: "edit", model: [submissionInstance: submissionInstance])
                return
            }
        }
        submissionInstance.properties = params
        if (!submissionInstance.save(flush: true)) {
            render(view: "edit", model: [submissionInstance: submissionInstance])
            return
        }
        flash.message = message(code: 'default.updated.message', args: [message(code: 'submission.label', default: 'Submission'), submissionInstance.id])
        redirect(action: "show", id: submissionInstance.id)
    }

	@Secured(['ROLE_ADMIN'])
    def delete(Long id) {
        def submissionInstance = Submission.get(id)
        if (!submissionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'submission.label', default: 'Submission'), id])
            redirect(action: "list")
            return
        }
        try {
            submissionInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'submission.label', default: 'Submission'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'submission.label', default: 'Submission'), id])
            redirect(action: "show", id: id)
        }
    }
}