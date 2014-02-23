package dcc

import grails.plugins.springsecurity.Secured;
import org.springframework.dao.DataIntegrityViolationException

class RankController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	
	def rankService

	/*
	 * This controller is largely unused.
	 * All CRUD functions that deal with ranks are in the other controllers.
	 */
	
    @Secured(['ROLE_ADMIN'])
    def index() {
		flash.message = "This method is inaccessible"
		redirect(url: '/')
		return
    }

	@Secured(['ROLE_ADMIN'])
	def list(Integer max) {
		flash.message = "This method is inaccessible"
		redirect(url: '/')
		return
	}

    @Secured(['ROLE_ADMIN'])
	def create() {
		flash.message = "This method is inaccessible"
		redirect(url: '/')
		return
	}

	@Secured(['ROLE_ADMIN'])
	def save() {
		flash.message = "This method is inaccessible"
		redirect(url: '/')
		return
	}

	/*
	 * Used to show individual ranks from a judge.
	 * Called from admin/showSubmission if the Submission the admin is viewing has a rank/has ranks 
	 */
    @Secured(['ROLE_ADMIN'])
	def show(Long id) {
		def result = rankService.show(id)
		if (!result.error) {
			return [rankInstance: result.rankInstance]
		}
	   flash.message = message(code: result.error.code, args: result.error.args)
		redirect(url: '/')
	   return
	}

    @Secured(['ROLE_ADMIN'])
	def edit(Long id) {
		flash.message = "This method is inaccessible"
		redirect(url: '/')
		return
	}

    @Secured(['ROLE_ADMIN'])
	def update(Long id, Long version) {
		flash.message = "This method is inaccessible"
		redirect(url: '/')
		return
	}

    @Secured(['ROLE_ADMIN'])
	def delete(Long id) {
		flash.message = "This method is inaccessible"
		redirect(url: '/')
		return
	}
}