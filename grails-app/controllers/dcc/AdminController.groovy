package dcc

import grails.plugins.springsecurity.Secured;
import org.springframework.dao.DataIntegrityViolationException

class AdminController {
	
	def springSecurityService
	
	/*
	 * Grab a count of how many submissions are in the database.
	 * Display this on the Admin homepage
	 */
	@Secured(['ROLE_ADMIN'])
	def index(){
		def submissions = Submission.count()
		def subs = (submissions <= 0) ? "Submissions are " : (submissions > 1) ? "Submissions are " : "Submission is "
		def sub = "$submissions $subs"

		[submissionsText:sub]
	}

	/*
	 * Get all the submissions
	 * Sort the submissions by category and then grandTotal
	 * Display a unique list of all the categories in the database
	 * Put the category list in the session so we don't have to keep querying the database to build the list
	 * When a user clicks on one of the categories, query the database for any submission that has that category
	 * Display the submissions found below the list of categories
	 */
	@Secured(['ROLE_ADMIN'])
	def showScoring(){
		if(params.category){
			def categoryList = session.categoryList ?: Submission.withCriteria { projections{ distinct "category" }; ne "category",""; order "category";}
			def submissionList = Submission.findAllByCategory("${params.category}")
			def submissionListTotal = Submission.countByCategory("${params.category}")
			submissionList.sort{ a,b ->
				b.grandTotal <=> a.grandTotal ?:
				a.dateCreated <=> b.dateCreated
			}
			params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
			params.offset = Math.min( params.offset ? params.offset.toInteger() : 0,  100)
			def start = params.offset
			def fin = params.offset + params.max - 1 //must do this to make true max of 10 because .getAt() does an inclusive range
			fin = (fin >= submissionListTotal) ? submissionListTotal - 1 : fin
			submissionList = submissionList.getAt(start..fin)

			[categoryList:categoryList,submissionList:submissionList,haveCategory:true,category:params.category,submissionListTotal:submissionListTotal]
		}
		else{
			def categoryList = Submission.withCriteria {
				projections{ distinct "category" }
				ne "category",""
				order "category"
			}
			session['categoryList'] = categoryList
			[categoryList:categoryList]
		}
	}
/*	def showScoring(){
		if(params.category){
			def categoryList = session.categoryList ?: Submission.withCriteria { projections{ distinct "category" }; ne "category",""; order "category";}
			def submissionList = Submission.findAllByCategory("${params.category}",[max:params.max ?: 10,offset: params.offset?: 0])
			def submissionListTotal = Submission.countByCategory("${params.category}")
			submissionList.each{ a ->
				def total = 0
				a.ranks.total.each{ b ->
					total += b.toInteger()
				}
				a.metaClass.grandTotal = total
			}
			submissionList.sort{ a,b ->
				a.category <=> b.category ?:
				b.grandTotal <=> a.grandTotal ?:
				a.dateCreated <=> b.dateCreated
			}
			[categoryList:categoryList,submissionList:submissionList,haveCategory:true,category:params.category,submissionListTotal:submissionListTotal]
		}
		else{
			def categoryList = Submission.withCriteria {
				projections{ distinct "category" }
				ne "category",""
				order "category"
			}
			session['categoryList'] = categoryList
			[categoryList:categoryList]
		}
	} */

	/*
	 * View an individual submission as an Admin
	 * Not functionally different than the Submission controller's show method
	 * Just makes it so the url is a little different, just to make it a little unique
	 */
	@Secured(['ROLE_ADMIN'])
	def showSubmission(Long id){
		def submissionInstance = Submission.get(id)
		if (!submissionInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'submission.label', default: 'Submission'), id])
			redirect(action: "showScoring")
			return
		}
		[submissionInstance: submissionInstance]
	}

	/*
	 * Show a list of Admin's in the database
	 */
	@Secured(['ROLE_ADMIN'])
	def list(Integer max,Integer offset) {
		params.max = Math.min(max ?: 10, 100)
		params.offset = Math.min(offset ?: 0, 100)
		def role = SecRole.findByAuthority("ROLE_ADMIN")
		def susr = SecUserSecRole.findAllBySecRole(role, [max: params.max ?: 10,offset: params.offset ?: 0])
		def susrCount = SecUserSecRole.countBySecRole(role)
		[secUserInstanceList: susr.secUser,secUserInstanceTotal:susrCount]
	}

	/*
	 * Create a new, blank secUserInstance to create an admin.
	 */
	@Secured(['ROLE_ADMIN'])
	def create() {
		[secUserInstance: new SecUser(params)]
	}

	/*
	 * Create a new secUserInstance and fill up the values from the
	 * params list which are sent over by posting the form on the create page.
	 */
	@Secured(['ROLE_ADMIN'])
	def save() {
		def secUserInstance = new SecUser(params)
		if (!secUserInstance.save(flush: true)) {
			render(view: "create", model: [secUserInstance: secUserInstance])
			return
		}
		def adminRole = SecRole.findByAuthority("ROLE_ADMIN") ?: new SecRole(authority:"ROLE_ADMIN").save(flush:true)
		SecUserSecRole.create secUserInstance,adminRole

		flash.message = "Admin $secUserInstance.username, has been created!"
		redirect(action: "show", id: secUserInstance.id)
	}
	/*
	 * Show an individual admin to show extra properties of the admin, edit the admin, or delete the admin
	 */
	@Secured(['ROLE_ADMIN'])
	def show(Long id) {
		def secUserInstance = SecUser.get(id)
		if (!secUserInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'admin.label', default: 'Admin'), id])
			redirect(action: "list")
			return
		}
		[secUserInstance: secUserInstance]
	}

	/*
	 * Edit the admin that was chosen in the show method
	 */
	@Secured(['ROLE_ADMIN'])
	def edit(Long id) {
		def secUserInstance = SecUser.get(id)
		if (!secUserInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'admin.label', default: 'Admin'), id])
			redirect(action: "list")
			return
		}
		[secUserInstance: secUserInstance]
	}

	/*
	 * Save any changes made to the admin from the edit method
	 */
	@Secured(['ROLE_ADMIN'])
	def update(Long id, Long version) {
		def secUserInstance = SecUser.get(id)
		if (!secUserInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'admin.label', default: 'Admin'), id])
			redirect(action: "list")
			return
		}
		if (version != null) {
			if (secUserInstance.version > version) {
				secUserInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
						  [message(code: 'admin.label', default: 'SecUser')] as Object[],
						  "Another user has updated this Submission while you were editing")
				render(view: "edit", model: [secUserInstance: secUserInstance])
				return
			}
		}
		secUserInstance.properties = params
		if (!secUserInstance.save(flush: true)) {
			render(view: "edit", model: [secUserInstance: secUserInstance])
			return
		}
		flash.message = message(code: 'default.updated.message', args: [message(code: 'admin.label', default: 'Admin'), secUserInstance.id])
		redirect(action: "show", id: secUserInstance.id)
	}

	/*
	 * Find the admin in the SecUser table.
	 * Find the adminRole.
	 * Remove the record from the SecUserSecRole mapping table, then remove the admin from the SecUser table.
	 * It must be done in this order due to a foreign key restraint being enforced.  Deleting the admin first will fail.
	 * If there is no adminRole, remove any record related to that user from the SecUserSecRole table, regardless of the role(s) the record has.
	 */
	@Secured(['ROLE_ADMIN'])
	def delete(Long id) {
		def secUserInstance = SecUser.get(id)
		if (!secUserInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'admin.label', default: 'Admin'), id])
			redirect(action: "list")
			return
		}
		try {
			def adminRole = SecRole.findByAuthority("ROLE_ADMIN")
			if(adminRole){
				SecUserSecRole.remove secUserInstance,adminRole
				secUserInstance.delete(flush: true)
				flash.message = message(code: 'default.deleted.message', args: [message(code: 'admin.label', default: 'Admin'), id])
				redirect(action: "list")
			}
			else{
				SecUserSecRole.removeAll secUserInstance
				secUserInstance.delete(flush: true)
				flash.message = message(code: 'default.deleted.message', args: [message(code: 'admin.label', default: 'Admin'), id])
				redirect(action: "list")
			}
		}
		catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'admin.label', default: 'Admin'), id])
			redirect(action: "show", id: id)
		}
	}
}
