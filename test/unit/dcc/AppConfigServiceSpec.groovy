package dcc

import org.junit.*
import spock.lang.*
import grails.test.mixin.*
import grails.buildtestdata.mixin.Build

@TestFor(AppConfigService)
@Build(AppConfig)
class AppConfigServiceSpec extends Specification{
    def params = [:]
    Integer max

    def populateValidParams(params) {
      assert params != null
        params.configKey = "testKey"
        params.configValue = "Test Value"
        params.remark = "This is only a test"
        params.max = 10
    }


  def "list 0 config values"(){
    given: "0 config values exist"

    when: "list method is called"
      def result = service.list(max,params)

    then: "Zero configuration instances are returned"
      result.appConfigInstanceTotal == 0
  }

  def "list 2 config values"(){
    given: "2 config values exist"
      AppConfig.build()
      AppConfig.build()

    when: "list method is called"
      def result = service.list(max,params)

    then: "Two configuration instances are returned"
      result.appConfigInstanceTotal == 2
  }

  def "Create method called with no data"(){
    given: "no params are passed"

    when: "create method is called"
      def result = service.create(params)
    
    then: "empty appConfigInstance is returned"
      result != null
      result.appConfigInstance.id == null 
  }

   def "Create method called with data"(){
    given: "params are passed"
      populateValidParams(params)

    when: "create method is called"
      def result = service.create(params)
    
    then: "appConfigInstance returned with some data"
      result != null
      result.appConfigInstance.configKey == 'testKey'
      AppConfig.count() == 0
  }

 def "save called with no data"(){
    given: "no params are passed"

    when: "save method is called"
      def result = service.save(params)
  
    then: "method failure is returned"
      result.error.code == "default.method.failure"
  }

 def "save called with missing required data"(){
    given: "some params are passed"
      populateValidParams(params)
    when: "required fields are blank and save method is called"
      params.configKey=""
       def result = service.save(params)
  
    then: "method failure is returned"
         result.error.code ==  "default.method.failure"
  }

 def "save is called with valid values"(){
    given: "all valid params are passed"
      populateValidParams(params)

    when: "save method is called"
       def result = service.save(params)
  
    then: "saved instance is returned"
         result.appConfigInstance.id ==  1
  }

 def "show is called with no values"(){
    given: "no values passed"

    when: "show method is called"
       def result = service.show()
  
    then: "not found message is returned"
         result.error.code == "default.not.found.message"
  }

 def "show is called with invalid id"(){
    given: "an invalid id"
    AppConfig.build()
    Integer id = 40

    when: "show method is called"
       def result = service.show(id)
  
    then: "not found message is returned"
         result.error.code == "default.not.found.message"
  }

 def "show is called with valid id"(){
    given: "a valid id"
      def appConfig = AppConfig.build()

    when: "id is passed to show method"
       def result = service.show(appConfig.id)
  
    then: "instance is returned"
         result.appConfigInstance.id == 1
  }

 def "edit is called with no values"(){
    given: "no values passed"

    when: "edit method is called"
       def result = service.edit()
  
    then: "not found message is returned"
         result.error.code == "default.not.found.message"
  }

 def "edit is called with invalid id"(){
    given: "an invalid id"
    AppConfig.build()
    Integer id = 40

    when: "edit method is called"
       def result = service.edit(id)
  
    then: "not found message is returned"
         result.error.code == "default.not.found.message"
  }

 def "edit is called with valid id"(){
    given: "a valid id"
      def appConfig = AppConfig.build()

    when: "id is passed to edit method"
       def result = service.edit(appConfig.id)
  
    then: "instance is returned"
         result.appConfigInstance.id == 1
  }

 def "update is called with no values"(){
    given: "no values"
    Integer id

    when: "update method is called"
       def result = service.update(id,params)
  
    then: "not found message is returned"
         result.error.code == "default.not.found.message"
  }

 def "update is called with invalid id"(){
    given: "an invalid id"
    AppConfig.build()
    Integer id = 40

    when: "update method is called"
       def result = service.update(id,params)
  
    then: "not found message is returned"
         result.error.code == "default.not.found.message"
  }

  def "update is called with valid id, invalid params"(){
    given: "a valid instance"
    def appConfig = AppConfig.build()

    when: "update method is called with invalid data"
      params.configKey = ""
       def result = service.update(appConfig.id,params)
  
    then: "method failure is returned"
         result.error.code == "default.method.failure"
  }

  def "update is called with no valid id, valid params"(){
    given: "a valid instance"
    def appConfig = AppConfig.build()

    when: "update method is called"
      params.configValue = "Updated value"
       def result = service.update(appConfig.id,params)
  
    then: "updated instance is returned"
         result.appConfigInstance.configValue == "Updated value"
  }
def "delete is called with no values"(){
    given: "no data"

    when: "delete method is called"
       def result = service.delete()
  
    then: "not found message is returned"
         result.error.code == "default.not.found.message"
  }

def "delete is called with invalid id"(){
    given: "an invalid id"
    AppConfig.build()
    Integer id = 40

    when: "delete method is called"
       def result = service.delete(id)
  
    then: "not found message is returned"
         result.error.code == "default.not.found.message"
  }

def "delete is called with valid id"(){
    given: "a valid id"
      def appConfig = AppConfig.build()

    when: "delete method is called"
       def result = service.delete(appConfig.id)
  
    then: "instance of deleted object is returned"
         result.appConfigInstance.id == 1
         AppConfig.count() == 0
  }

}