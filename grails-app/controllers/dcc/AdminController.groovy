package dcc

import grails.plugins.springsecurity.Secured;
import org.springframework.dao.DataIntegrityViolationException

class AdminController {
	
	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def adminService

	/*
	 * Call index in adminService.
	 * Display some text built in the service.
	 * If there is an error, redirect to the landing page.
	 */
    @Secured(['ROLE_ADMIN'])
    def index(){
		def result = adminService.index() 
		if(!result.error){
			return [submissionsText:result.sub]
		}
		flash.message = message(code: result.error.code, args: result.error.args)
		redirect(url:"/")
	}

	/*
	 * Call showScoring method in adminService.
	 * Return a list of categories and set a session variable in order to help reduce database reads.
	 * Otherwise, return a list of categories and a list of submissions for the category that was chosen by the user.
	 */
	@Secured(['ROLE_ADMIN'])
	def showScoring(){
		def result = adminService.showScoring(params,session.categoryList)
		if(!result.error) {
			return [categoryList:result.categoryList,submissionList:result.submissionList,haveCategory:result.haveCategory,category:result.category,submissionListTotal:result.submissionListTotal]
		}
		flash.message = message(code: result.error.code, args: result.error.args)
		redirect(action:'index')
	}

	/*
	 * Call showSubmission in adminService.
	 * Return a submissionsInstance which will show more information about that particular submission.
	 * If there are any errors, redirect to the showScoring page and display a message there about what went wrong.
	 */
    @Secured(['ROLE_ADMIN'])
	def showSubmission(Long id){
		def result = adminService.showSubmission(params?.id?.toLong())
		if (!result.error) {
			return [submissionInstance: result.submissionInstance]
		}
		flash.message = message(code: result.error.code, args: result.error.args)
		redirect(action: "showScoring")
	}

	/*
	 * Call updateWinnerStatus in adminService.
	 * Find the submission and update the value of winner.
	 * If there are any errors, redirect to the showSubmission page and display a message there about what went wrong.
	 */
    @Secured(['ROLE_ADMIN'])
	def updateWinnerStatus(){
		def result = adminService.updateWinnerStatus(params)
		if (!result.error) {
			flash.message = "Successfully updated winner status."
			redirect(action: "showSubmission",id:result.submissionInstance?.id)
			return
		}
		flash.message = message(code: result.error.code, args: result.error.args)
		redirect(action: "showSubmission",id:result.submissionInstance?.id)
		return
	}

	 /*
	  * Update the grand_total of each submission to make it current 
	  */
    @Secured(['ROLE_ADMIN'])
	def updateGrandTotal(){
		try{
			def result = adminService.updateGrandTotal()
			if (!result.error) {
				flash.message = "Successfully updated grand total for each submission."
				redirect(action: "index")
				return
			}
			flash.message = message(code: result.error.code, args: result.error.args)
			redirect(action: "index")
			return			
		} catch(Exception e){
			flash.message = "Could not update grand total for each submission."
			redirect(action:"index")
			return
		}
	}

	/*
	 * Call list in adminService to get a list of current Admins in the database.
	 * If there are any errors, redirect to the admin homepage.
	 */
	@Secured(['ROLE_ADMIN'])
	def list(Integer max,Integer offset) {
		def result = adminService.list(max, offset, params)
		if(result.secUserCount == 0) {
			flash.message = "No Admins found.  Create a new admin."
			redirect(action:'create')
			return
		}
		else if(!result.error){
			return [secUserInstanceList: result.secUser,secUserInstanceTotal:result.secUserCount]
		}
		flash.message = message(code: result.error.code, args: result.error.args)
		redirect(action: "index")
	}

	/*
	 * Call create in adminService
	 * 
	 */
	@Secured(['ROLE_ADMIN'])
	def create() {
		def result = adminService.create(params)
		if(!result.error){
			return [secUserInstance: result.secUserInstance]
		}
		flash.message = message(code: result.error.code, args: result.error.args)
		redirect(action:'list')
		return
	}

	/*
	 * Call save in adminService.
	 * If result has no errors, redirect to show the record just created.
	 * If there are errors or there is an exception thrown,
	 * render the create view with any values from the previous request.
	 */
	@Secured(['ROLE_ADMIN'])
	def save() {
		try{
			def result = adminService.save(params)
			if (!result.error) {
				flash.message = "Admin $result.secUserInstance.username, has been created!"
				redirect(action: "show", id: result?.secUserInstance?.id)
				return
			}
			flash.message = message(code: result.error.code, args: result.error.args)
			render(view: "create", model: [secUserInstance: result.secUserInstance])
			return
		}
		catch(Exception e){
			flash.message = message(code: "default.method.failure", args: ["Administrator could not be created.","Verify you are using valid information in the fields below."])
			render(view:'create', model:[secUserInstance:new SecUser(params)])
			return
		}
	}

	/*
 	 * Call show in adminService.
 	 * Return an individual admin to show extra properties of the admin, edit the admin, or delete the admin
 	 */
 	@Secured(['ROLE_ADMIN'])
 	def show(Long id) {
 		def result = adminService.show(id)
 		if (!result.error) {
			 return [secUserInstance: result.secUserInstance]
 		}
		flash.message = message(code: result.error.code, args: result.error.args)
		redirect(action: "list")
		return
 	}

	/*
	 * Edit the admin that was chosen in the show method
	 */
	@Secured(['ROLE_ADMIN'])
	def edit(Long id) {
 		def result = adminService.edit(id)
 		if (!result.error) {
			 return [secUserInstance: result.secUserInstance]
 		}
		flash.message = message(code: result.error.code, args: result.error.args)
		redirect(action: "list")
		return
	}

	/*
	 * Call update in adminService.
	 * If there are no errors, redirect to show the record that was just updated.
	 * If there are errors or an Exception is thrown, redirect to the edit stage.
	 */
	@Secured(['ROLE_ADMIN'])
	def update(Long id, Long version) {
		try{
			def result = adminService.update(id, version, params)
			if(!result.error){
				flash.message = message(code: 'default.updated.message', args: [message(code: 'admin.label', default: 'Admin'), params.id])
				redirect(action:"show", id: id)
				return
			}
			if(!result.secUserInstance) {
				flash.message = message(code: result.error.code, args: result.error.args)
				redirect(action: "list")
				return
			}
			flash.message = message(code: result.error.code, args: result.error.args)
			redirect(action: "edit", id: id)
			return
		}
		catch(Exception e){
			flash.message = message(code: "default.method.failure", args: ["Administrator could not be updated.","Verify correct data is being used in the fields below."])
			redirect(action: "edit", id: id)
			return

		}
	}
	
	/*
	 * Call delete in adminService.
	 * If there are no errors, redirect to list to show the rest of the administrators.
	 * If there are errors, redirect back to show.
	 */
	@Secured(['ROLE_ADMIN'])
	def delete(Long id) {
		try {
			def result = adminService.delete(id)
			//Success
			if(!result.error){
				flash.message = message(code: 'default.deleted.message', args: [message(code: 'admin.label', default: 'Admin'), id])
				redirect(action: "list")
				return
			}
			//Could not find by id
			if(!result.secUserInstance) {
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
			flash.message = message(code: "default.method.failure", args: ["Administrator could not be deleted.",""])			
			redirect(action: "show", id: id)
			return
		}
	}
}