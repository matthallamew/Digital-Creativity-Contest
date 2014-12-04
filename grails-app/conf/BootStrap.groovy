import dcc.Judge
import dcc.Rank
import dcc.SecRole
import dcc.SecUser
import dcc.Submission
import dcc.SecUserSecRole
import dcc.AppConfig
import grails.util.Environment

class BootStrap {

    def init = { servletContext ->
    		switch(Environment.getCurrent()){
    			case "DEVELOPMENT":
				(1..25).each{
					new Submission(category:"Whatever",title:"My Subbmission $it",author:"Author $it",link:"http://youtu.be/5L28TM48bF0",emailAddr:"email1@domain.com").save(flush:true)
				}

				Judge j = new Judge(username:'peterj',password:'a',firstName:'PeterJ',lastName:'Griffon',enabled:true).save(flush:true)
				//Judge j = Judge.findByUsername('peterj')
				Submission s1 = Submission.get(1)
				new Rank(skill:6,creativity:6,aesthetic:6,purpose:6,total:24,submissions:s1,judges:j).save(flush:true)

				def adminRole = SecRole.findByAuthority("ROLE_ADMIN") ?: new SecRole(authority:"ROLE_ADMIN").save(flush:true)
				def judgeRole = SecRole.findByAuthority("ROLE_JUDGE") ?: new SecRole(authority:"ROLE_JUDGE").save(flush:true)

				def admin = new SecUser(username:'tsmith',password:'pssdubdd',firstName:'Todd',lastName:'Smith',enabled:true).save(flush:true)

				SecUserSecRole.create admin,adminRole
				SecUserSecRole.create j, judgeRole
    				AppConfig.findByConfigKey("cutOffDate") ?: new AppConfig(configKey:"cutOffDate",configValue:"04/26/2015 23:59:59").save(flush:true)
    				AppConfig.findByConfigKeyAndConfigValue("submissionCategory","Static") ?: new AppConfig(configKey:"submissionCategory",configValue:"Static").save(flush:true)
    				AppConfig.findByConfigKeyAndConfigValue("submissionCategory","Multimedia") ?: new AppConfig(configKey:"submissionCategory",configValue:"Multimedia").save(flush:true)
    			break
    			case "PRODUCTION":
    				AppConfig.findByConfigKey("cutOffDate") ?: new AppConfig(configKey:"cutOffDate",configValue:"04/26/2015 23:59:59").save(flush:true)
    				AppConfig.findByConfigKeyAndConfigValue("submissionCategory","Static") ?: new AppConfig(configKey:"submissionCategory",configValue:"Static").save(flush:true)
    				AppConfig.findByConfigKeyAndConfigValue("submissionCategory","Multimedia") ?: new AppConfig(configKey:"submissionCategory",configValue:"Multimedia").save(flush:true)
    			break
    		}
	}
    def destroy = {
    }
}
