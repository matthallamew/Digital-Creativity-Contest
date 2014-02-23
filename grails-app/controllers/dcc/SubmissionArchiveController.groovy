package dcc

import grails.plugins.springsecurity.Secured;
import org.springframework.dao.DataIntegrityViolationException

class SubmissionArchiveController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	
	def submissionArchiveService
	
	@Secured(['ROLE_ADMIN'])
    def index() {
		render(view:'index')
    }

	/*
	 * Call archiveSubmissions in submissionArchiveService.
	 * If there are no errors, show the user how many submissions are in the archive.
	 * 
	 */
	@Secured(['ROLE_ADMIN'])
    def archiveSubmissions() {
		def result = submissionArchiveService.archiveSubmissions()
		if(!result.error){
			return [submissionsText: result.submissionsText]
		}
		flash.message = message(code: result.error.code, args: result.error.args)
		redirect(url: "/admin/")
		return
    }

	/*
	 * Call moveSubmissions in the submissionArchiveService.
	 * If there were no errors or exceptions thrown, redirect the user back to the SubmissionArchive home page.
	 * If there were errors or exceptions thrown, redirec the user back to the archiveSubmissions page.
	 */
	@Secured(['ROLE_ADMIN'])
    def moveSubmissions() {
		try{
			def result = submissionArchiveService.moveSubmissions(params)
			if(!result.error){
				flash.message = "Submissions have been successfully archived."
				redirect(action:"index")
				return
			}
			flash.message = message(code: result.error.code, args: result.error.args)
			redirect(action:"archiveSubmissions")
			return
		}
		catch(Exception e){
			flash.message = message(code: "default.method.failure", args: ["Submissions could not be archived.",""]) 
			redirect(action:"archiveSubmissions")
			return
		}
    }
	
	/*
	 * Clear all the submissions out of the archive.
	 * This permanently deletes the submissions from the archive.
	 */
	def clearArchive(){
		try {
			def result = submissionArchiveService.clearArchive()
			if(!result.error) {
				flash.message = "Removed ${result.deleteCount} from the archive."
				redirect(action:"index")
				return
			}
			flash.message = message(code:result.error.code,args:result.error.args)
			redirect(action:"index")
			return
		}
		catch(Exception e) {
			flash.message = message(code: "default.method.failure", args: ["Archive could not be cleared.",""]) 
			redirect(action:"index")
			return
		}
	}

	/*
	 * Anyone can use this method to view past submissions.
	 */
    def list(Integer max,Integer offset) {
		def result = submissionArchiveService.list(params)
		if(!result.error){
			return [submissionArchiveInstanceList: result.submissionArchiveInstanceList, submissionArchiveInstanceTotal: result.submissionArchiveInstanceTotal]
		}
		flash.message = message(code: result.error.code, args: result.error.args)
		redirect(action:"index")
	}

	/*
	 * Call listWinners in submissionArchiveService
	 * Show list of the winning submissions for past years.
	 */
    def listWinners(Integer max,Integer offset) {
		def result = submissionArchiveService.listWinners(max,offset,params)
		if(!result.error){
			return [submissionArchiveInstanceList: result.submissionArchiveInstanceList, submissionArchiveInstanceTotal: result.submissionArchiveInstanceTotal]
		}
		flash.message = message(code: result.error.code, args: result.error.args)
		redirect(action:"index")      
    }

	@Secured(['ROLE_ADMIN'])
    def create() {
		flash.message = "This method is inaccessible"
		redirect(action: 'index')
		return
    }

	@Secured(['ROLE_ADMIN'])
    def save() {
		flash.message = "This method is inaccessible"
		redirect(action: 'index')
		return
    }

	/*
	 * Anyone can use this method to view more details about a past submission.
	 */
    def show(Long id) {
		def result = submissionArchiveService.show(id)
		if (!result.error) {
			return [submissionArchiveInstance: result.submissionArchiveInstance]
		}
	   flash.message = message(code: result.error.code, args: result.error.args)
	   redirect(action: "list")
	   return
    }

	/*
	 * Call showWinner in submissionArchiveService
	 * Show a specific winning submission.
	 */
	def showWinner(Long id) {
		def result = submissionArchiveService.showWinner(id)
		if (!result.error) {
			return [submissionArchiveInstance: result.submissionArchiveInstance]
		}
	   flash.message = message(code: result.error.code, args: result.error.args)
	   redirect(action: "listWinners")
	   return
	}

	@Secured(['ROLE_ADMIN'])
    def edit(Long id) {
		flash.message = "This method is inaccessible"
		redirect(action: 'index')
		return
    }

	@Secured(['ROLE_ADMIN'])
    def update(Long id, Long version) {
		flash.message = "This method is inaccessible"
		redirect(action: 'index')
		return
    }

	@Secured(['ROLE_ADMIN'])
    def delete(Long id) {
		flash.message = "This method is inaccessible"
		redirect(action: 'index')
		return
    }
}