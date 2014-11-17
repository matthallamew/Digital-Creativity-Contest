package dcc

import org.springframework.dao.DataIntegrityViolationException
import grails.plugins.springsecurity.Secured
class AppConfigController {

    def appConfigService
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    @Secured(['ROLE_ADMIN'])
    def index() {
        redirect(action: "list", params: params)
    }

    @Secured(['ROLE_ADMIN'])
    def list(Integer max) {
        def result = appConfigService.list(max,params)
        if(!result.error){
            return [appConfigInstanceList: result.appConfigInstanceList, appConfigInstanceTotal: result.appConfigInstanceTotal]
        }
        flash.message = message(code: result.error.code, args:result.error.args)
        redirect (url:"/admin")
    }

    @Secured(['ROLE_ADMIN'])
    def create() {
        def result = appConfigService.create(params)
        if(!result.error){
            return [appConfigInstance: result.appConfigInstance]
        }
        flash.message = message(code:result.error.code,args:result.error.args)
        redirect(action:'list')
    }

    @Secured(['ROLE_ADMIN'])
    def save() {
        try {
            def result = appConfigService.save(params)
            if(!result.error){
                flash.message = message(code: 'default.created.message', args:[message(code: 'appConfig.label', default: 'Configuration Value'), result.appConfigInstance.configKey])
                redirect(action:"show",id:result.appConfigInstance.id)
                return
            }
            flash.message = message(code:result.error.code,args:result.error.args)
            render(view:"create",model:[appConfigInstance:result.appConfigInstance])
        }
        catch(Exception e) {
            flash.message = message(code:"default.method.failure", args:["Configuration value could not be created.","Verify correct is being used in the fields below."])
            render(view: "create", model:[appConfigInstance: new AppConfig(params)])
            return
        }
    }

    @Secured(['ROLE_ADMIN'])
    def show(Long id) {
        def result = appConfigService.show(id)
        if(!result.error){
            return [appConfigInstance: result.appConfigInstance]
        }
        flash.message = message(code: result.error.code,args: result.error.args)
        redirect(action:'list')
        return
    }

    @Secured(['ROLE_ADMIN'])
    def edit(Long id) {
        def result = appConfigService.edit(id)
        if(!result.error){
            return [appConfigInstance: result.appConfigInstance]
        }
        flash.message = message(code: result.error.code,args: result.error.args)
        redirect(action:'list')
        return
    }

    @Secured(['ROLE_ADMIN'])
    def update(Long id) {
        try{
            def result = appConfigService.update(id,params)
            // Success
            if(!result.error){
                flash.message = message(code: 'default.updated.message', args: [message(code: 'appConfig.label', default: 'Configuration Value'), result.appConfigInstance.configKey])
                redirect(action:"show", id: result.appConfigInstance.id)
                return
            }
            // Could not find by ID
            if(!result.appConfigInstance){
                flash.message = message(code:result.error.code,args:result.error.args)
                redirect(action:'list')
                return
            }
            // Could not update
            flash.message = message(code:result.error.code,args:result.error.args)
            render(view:'edit',model:[appConfigInstance:result.appConfigInstance])
            return            
        } catch(Exception e){
            flash.message = message(code:"default.method.failure",args:["Configuration value could not be updated.", "Verify correct data is being used in the fields below."])
            render(view:"edit",model:[appConfigInstance:new AppConfig(params)])
            return
        }
    }

    @Secured(['ROLE_ADMIN'])
    def delete(Long id) {
        try{
            def result = appConfigService.delete(id)
            // Success
            if(!result.error){
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'appConfig.label', default: 'Configuration Value'), result.appConfigInstance.configKey])
                redirect(action:"list")
                return
            }
            // Could not find by ID
            if(!result.appConfigInstance){
                flash.message = message(code:result.error.code,args:result.error.args)
                redirect(action:"list")
                return
            }
            // Found, could not delete
            flash.message = message(code:result.error.code,args:result.error.args)
            redirect(action:"show", id:result.appConfigInstance.id)
            return            
        } catch(Exception e){
            flash.message = message(code:"default.method.failure",args:["Configuration value could not be deleted",""])
            redirect(action:"show",id:result.appConfigInstance.id)
            return
        }
    }
}
