package dcc

import org.junit.*
import spock.lang.*
import grails.test.mixin.*
import grails.buildtestdata.mixin.Build

@TestFor(AdminService)
@Build([Judge,Submission,Rank,SecUser,SecRole,SecUserSecRole])
class AdminServiceSpec extends Specification{
    def params = [:]
    Integer max,offset,id,version


    def populateValidParams(params) {
      assert params != null
      params["category"] = "Cool"
      params["title"] = "Great Project"
      params["author"] = "Matt Dubb"
      params["emailAddr"] = "mdub@email.com"
      params["link"] = "https://coollinks.com"
      params["skill"] = 6
      params["creativity"] = 6
      params["aesthetic"] = 6
      params["purpose"] = 6
      params["total"] = 24
      params["authority"] = "ROLE_ADMIN"
      params["username"]='peterj'
      params["password"] = 'a'
      params["firstName"]='PeterJ'
      params["lastName"]='Griffon'
      params["enabled"]=true
      SecUser.metaClass.encodePassword = {'a'}

    }

  def "index is called with no data in db"(){
    given: "no submissions exist"

    when: "index method is called"
      def result = service.index()

    then: "count of submissions is 0"
      Submission.count() == 0
  }

  def "index is called with data in db"(){
    given: "3 submissions exist"
      Submission.build()
      Submission.build()
      Submission.build()

    when: "index method is called"
      def result = service.index()

    then: "count of submissions is 3"
      Submission.count() == 3
      result.sub == "3 Submissions are "
  }

  def "showScoring is called with no data in db"(){
    given: "no submissions exist"
      def categoryList = []

    when: "showScoring method is called"
      def result = service.showScoring(params,categoryList)

    then: "method failure is returned"
      Submission.count() == 0
  }

  def "showScoring is called with submissions, empty category"(){
    given: "2 submissions exist"
      def categoryList = []
      Submission.build()
      Submission.build()

    when: "showScoring method is called"
      def result = service.showScoring(params,categoryList)

    then: "category list is returned"
      result.categoryList[0] == "category"
  }

  def "showScoring is called with submissions, selected category"(){
    given: "2 submissions exist and a category is selected"
      Submission.build()
      Submission.build()
      def categoryList = ["category"]
      params.category = categoryList[0]

    when: "showScoring method is called"
      def result = service.showScoring(params,categoryList)

    then: "submission list,boolean, and category list is returned"
      result.submissionListTotal == 2
      result.categoryList[0] == "category"
      result.haveCategory == true
  }

 def "showSubmission is called with no values"(){
    given: "no values passed"

    when: "showSubmission method is called"
       def result = service.showSubmission()
  
    then: "not found message is returned"
         result.error.code == "default.not.found.message"
  }

 def "showSubmission is called with invalid id"(){
    given: "an invalid id"
    Submission.build()
    Integer id = 40

    when: "showSubmission method is called"
       def result = service.showSubmission(id)
  
    then: "not found message is returned"
         result.error.code == "default.not.found.message"
  }

 def "showSubmission is called with valid id"(){
    given: "a valid id"
      def submission = Submission.build()

    when: "id is passed to showSubmission method"
       def result = service.showSubmission(submission.id)
  
    then: "instance is returned"
         result.submissionInstance.id == 1
  }

  def "updateWinnerStatus is called when no data is in the db"(){
    given: "no data exists in the db"

    when: "updateWinnerStatus method is called"
      def result = service.updateWinnerStatus(params)

    then: "not found message is returned"
      result.error.code == "default.not.found.message"
  }

  def "updateWinnerStatus is called after someone else updated record"(){
    given: "data exists in the db"
      def submission = Submission.build().save(flush:true)
      params.version = -1
      params.id = submission.id

    when: "updateWinnerStatus method is called"
      def result = service.updateWinnerStatus(params)

    then: "optimistic locking message is returned"
      result.error.code == "default.optimistic.locking.failure"
  }

  def "updateWinnerStatus is called with valid winner status"(){
    given: "data exists in the db and a valid winner status is passed"
      def submission = Submission.build().save(flush:true)
      params.id = submission.id
      params.winner = true

    when: "updateWinnerStatus method is called"
      def result = service.updateWinnerStatus(params)

    then: "an updated instance is returned"
      result.submissionInstance.winner == true
  }

  def "updateGrandTotal is called with no data in the db"(){
    given: "no data in the db"

    when: "updateGrandTotal method is called"
      def result = service.updateGrandTotal()

    then: "method failure message is returned"
      result.error.code == "default.method.failure"
  }

  def "updateGrandTotal is called with data in the db"(){
    given: "data exists in the db"
      Submission.build()
      Submission.build()

    when: "updateGrandTotal method is called"
      def result = service.updateGrandTotal()

    then: "success is returned"
      result.success == true
  }

  def "list method is called with no data in the db"(){
    given: "no data exists in the db"

    when: "list method is called"
      def result = service.list(max,offset,params)

    then: "no instances are returned"
      result.secUserCount == 0
  }

  def "list method is called where 1 admins exist"(){
    given: "1 admin exists"
      populateValidParams(params)
      def sr1 = new SecRole(params).save(flush:true)
      def su1 = new SecUser(params)
      su1.metaClass.encodePassword={'b'}
      su1.save(flush:true)
      SecUserSecRole.create su1,sr1

    when: "list method is called"
      def result = service.list(max,offset,params)

    then: "1 instance is returned"
      result.secUserCount == 1
  }

  def "Create method called with no data"(){
    given: "no params are passed"

    when: "create method is called"
      def result = service.create(params)
    
    then: "empty instance is returned"
      result.secUserInstance.id == null 
  }

   def "Create method called with data"(){
    given: "params are passed"
      populateValidParams(params)

    when: "create method is called"
      def result = service.create(params)
    
    then: "Instance returned with some data"
      result.secUserInstance.username == 'peterj'
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
      params.username=""
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
         result.secUserInstance.id ==  1
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
      populateValidParams(params)
      def sr1 = new SecRole(params).save(flush:true)
      def su1 = new SecUser(params)
      su1.metaClass.encodePassword={'b'}
      su1.save(flush:true)
      SecUserSecRole.create su1,sr1
      id = 40

    when: "show method is called"
       def result = service.show(id)
  
    then: "not found message is returned"
         result.error.code == "default.not.found.message"
  }

 def "show is called with valid id"(){
    given: "a valid id"
      populateValidParams(params)
      def sr1 = new SecRole(params).save(flush:true)
      def su1 = new SecUser(params)
      su1.metaClass.encodePassword={'b'}
      su1.save(flush:true)
      SecUserSecRole.create su1,sr1

    when: "id is passed to show method"
       def result = service.show(su1.id)
  
    then: "instance is returned"
         result.secUserInstance.id == 1
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
      populateValidParams(params)
      def sr1 = new SecRole(params).save(flush:true)
      def su1 = new SecUser(params)
      su1.metaClass.encodePassword={'b'}
      su1.save(flush:true)
      SecUserSecRole.create su1,sr1
      id = 40

    when: "edit method is called"
       def result = service.edit(id)
  
    then: "not found message is returned"
         result.error.code == "default.not.found.message"
  }

 def "edit is called with valid id"(){
    given: "a valid id"
      populateValidParams(params)
      def sr1 = new SecRole(params).save(flush:true)
      def su1 = new SecUser(params)
      su1.metaClass.encodePassword={'b'}
      su1.save(flush:true)
      SecUserSecRole.create su1,sr1

    when: "id is passed to edit method"
       def result = service.edit(su1.id)
  
    then: "instance is returned"
         result.secUserInstance.id == 1
  }

 def "update is called with no values"(){
    given: "no values"

    when: "update method is called"
      def result = service.update(id,version,params)
  
    then: "not found message is returned"
      result.error.code == "default.not.found.message"
  }

 def "update is called with invalid id"(){
    given: "an invalid id"
      populateValidParams(params)
      def sr1 = new SecRole(params).save(flush:true)
      def su1 = new SecUser(params)
      su1.metaClass.encodePassword={'b'}
      su1.save(flush:true)
      SecUserSecRole.create su1,sr1
      id = 40

    when: "update method is called"
       def result = service.update(id,version,params)
  
    then: "not found message is returned"
         result.error.code == "default.not.found.message"
  }

  def "update is called with valid id, invalid params"(){
    given: "a valid instance"
      populateValidParams(params)
      def sr1 = new SecRole(params).save(flush:true)
      def su1 = new SecUser(params)
      su1.metaClass.encodePassword={'b'}
      su1.save(flush:true)
      SecUserSecRole.create su1,sr1

    when: "update method is called with invalid data"
      params.username = ""
       def result = service.update(su1.id,version,params)
  
    then: "method failure is returned"
         result.error.code == "default.method.failure"
  }

  def "update is called with valid id, stale version"(){
    given: "a valid instance"
      populateValidParams(params)
      def sr1 = new SecRole(params).save(flush:true)
      def su1 = new SecUser(params)
      su1.metaClass.encodePassword={'b'}
      su1.save(flush:true)
      SecUserSecRole.create su1,sr1
      version = -1

    when: "update method is called with invalid data"
      params.username = ""
       def result = service.update(su1.id,version,params)
  
    then: "optimistic locking failure is returned"
         result.error.code == "default.optimistic.locking.failure"
  }

  def "update is called with valid id, valid params"(){
    given: "a valid instance"
      populateValidParams(params)
      def sr1 = new SecRole(params).save(flush:true)
      def su1 = new SecUser(params)
      su1.metaClass.encodePassword={'b'}
      su1.metaClass.isDirty= {false}
      su1.save(flush:true)
      SecUserSecRole.create su1,sr1

    when: "update method is called"
      params.lastName = "Newlastname"
       def result = service.update(su1.id,version,params)
  
    then: "updated instance is returned"
         result.secUserInstance.lastName == "Newlastname"
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
      populateValidParams(params)
      def sr1 = new SecRole(params).save(flush:true)
      def su1 = new SecUser(params)
      su1.metaClass.encodePassword={'b'}
      su1.save(flush:true)
      SecUserSecRole.create su1,sr1
      id = 40

    when: "delete method is called"
       def result = service.delete(id)
  
    then: "not found message is returned"
         result.error.code == "default.not.found.message"
  }

def "delete is called with valid id"(){
    given: "a valid id"
      populateValidParams(params)
      def sr1 = new SecRole(params).save(flush:true)
      def su1 = new SecUser(params)
      su1.metaClass.encodePassword={'b'}
      su1.save(flush:true)
      SecUserSecRole.create su1,sr1
      Integer beforeCount = SecUser.count()

    when: "delete method is called"
       def result = service.delete(su1.id)
  
    then: "instance of deleted object is returned"
         result.secUserInstance.id == 1
         SecUser.count() == 0
         beforeCount == 1
  }

}