package dcc

import grails.plugins.springsecurity.Secured;
import org.springframework.dao.DataIntegrityViolationException

class RankController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	/*
	 * This controller is largely unused.
	 * All CRUD functions that deal with ranks are in the other controllers.
	 * The functions can be used if the logged in user is an admin, but he or she would have to know the exact URL.
	 */
	
    @Secured(['ROLE_ADMIN'])
    def index() {
        redirect(action: "list", params: params)
    }

    @Secured(['ROLE_ADMIN'])
    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [rankInstanceList: Rank.list(params), rankInstanceTotal: Rank.count()]
    }

    @Secured(['ROLE_ADMIN'])
    def create() {
        [rankInstance: new Rank(params)]
    }

    @Secured(['ROLE_ADMIN'])
    def save() {
        def rankInstance = new Rank(params)
        if (!rankInstance.save(flush: true)) {
            render(view: "create", model: [rankInstance: rankInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'rank.label', default: 'Rank'), rankInstance.id])
        redirect(action: "show", id: rankInstance.id)
    }

	/*
	 * Used to show individual ranks from a judge.
	 * Called from admin/showSubmission if the Submission the admin is viewing has a rank/has ranks 
	 */
    @Secured(['ROLE_ADMIN'])
    def show(Long id) {
        def rankInstance = Rank.get(id)
        if (!rankInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'rank.label', default: 'Rank'), id])
            redirect(action: "list")
            return
        }

        [rankInstance: rankInstance]
    }

    @Secured(['ROLE_ADMIN'])
    def edit(Long id) {
        def rankInstance = Rank.get(id)
        if (!rankInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'rank.label', default: 'Rank'), id])
            redirect(action: "list")
            return
        }

        [rankInstance: rankInstance]
    }

    @Secured(['ROLE_ADMIN'])
    def update(Long id, Long version) {
        def rankInstance = Rank.get(id)
        if (!rankInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'rank.label', default: 'Rank'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (rankInstance.version > version) {
                rankInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'rank.label', default: 'Rank')] as Object[],
                          "Another user has updated this Rank while you were editing")
                render(view: "edit", model: [rankInstance: rankInstance])
                return
            }
        }

        rankInstance.properties = params

        if (!rankInstance.save(flush: true)) {
            render(view: "edit", model: [rankInstance: rankInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'rank.label', default: 'Rank'), rankInstance.id])
        redirect(action: "show", id: rankInstance.id)
    }

    @Secured(['ROLE_ADMIN'])
    def delete(Long id) {
        def rankInstance = Rank.get(id)
        if (!rankInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'rank.label', default: 'Rank'), id])
            redirect(action: "list")
            return
        }

        try {
            rankInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'rank.label', default: 'Rank'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'rank.label', default: 'Rank'), id])
            redirect(action: "show", id: id)
        }
    }
}