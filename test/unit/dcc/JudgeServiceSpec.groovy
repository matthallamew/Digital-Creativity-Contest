package dcc

import org.junit.*
import spock.lang.*
import grails.test.mixin.*
import grails.buildtestdata.mixin.Build

@TestFor(JudgeService)
@Build([Judge,Submission,SecUser,SecRole,SecUserSecRole,AppConfig])
class JudgeServiceSpec extends Specification{
    def params = [:]
    Integer max,offset,id,version

    def populateValidParams(params) {
      assert params != null
      params["authority"] = "ROLE_JUDGE"
      new AppConfig(configKey:"cutOffDate",configValue:"04/26/2015 23:59:59").save(flush:true)
      params["cutOffDate"] = AppConfig.findByConfigKey("cutOffDate")?.configValue
      SecUser.metaClass.encodePassword = {'a'}
      Judge.metaClass.encodePassword = {'a'}
      def springSecurityService =[principal:Judge.build().save(flush:true)]
      service.springSecurityService = springSecurityService
  }

  def "rankSubmissions method is called with no data in the db"(){
    given: "no data exists in the db"

    when: "i method is called"
      def result = service.rankSubmission()

    then: "Zero instances are returned"
      println result
      result != null
  }


}