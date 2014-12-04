package dcc

import org.junit.*
import spock.lang.*
import grails.test.mixin.*
import grails.buildtestdata.mixin.Build

@TestFor(RankService)
@Build([Rank,Judge,Submission])
class RankServiceSpec extends Specification{
    def params = [:]
    Integer max,id

    def populateValidParams(params) {
      Judge.metaClass.encodePassword = {'a'}
      Judge judge = new Judge(username:'peterj',password:'a',firstName:'PeterJ',lastName:'Griffon',enabled:true)
      judge.metaClass.encodePassword = {'b'}
      judge.save(flush:true)
      Submission submission = Submission.build().save(flush:true)
      assert params != null
      params["skill"] = 6
      params["creativity"] = 6
      params["aesthetic"] = 6
      params["purpose"] = 6
      params["total"] = 24
      params["judges"] = judge
      params["submissions"] = submission
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
      Rank.build()
      id = 40

    when: "show method is called"
       def result = service.show(id)
  
    then: "not found message is returned"
         result.error.code == "default.not.found.message"
  }

 def "show is called with valid id"(){
    given: "a valid id"
      populateValidParams(params)
      def rank = Rank.build()

    when: "id is passed to show method"
      def result = service.show(rank.id)

    then: "instance is returned"
      result.rankInstance.id == 1
  }

}