package dcc

import org.junit.*
import grails.test.mixin.*

@TestFor(JudgeController)
@Mock([Judge,Submission,SecUser,SecRole,SecUserSecRole,AppConfig])
class JudgeControllerTests {
	
	@Before
	void setUp() {
        controller.judgeService = new JudgeService()
        Judge.metaClass.encodePassword = {'a'}
        // params["username"]='chrisg'
        // params["password"] = 'a'
        // params["firstName"]='Chris'
        // params["lastName"]='Griffon'
        // params["password"]='pass'
        // params["enabled"]=true
        // params["authority"] = "ROLE_JUDGE"
        // def loggedInUser = new Judge(params).save(flush:true)
        // def springSecurityService =[ encodePassword: 'password',
        //                   reauthenticate: { String u -> true},
        //                   loggedIn: true,
        //                   principal: loggedInUser]
        // controller.judgeService.springSecurityService = springSecurityService
        // println loggedInUser
	}

    def populateValidParams(params) {
        assert params != null
		params["username"]='peterj'
		params["password"] = 'a'
		params["firstName"]='PeterJ'
		params["lastName"]='Griffon'
		params["password"]='pass'
		params["enabled"]=true
		params["authority"] = "ROLE_JUDGE"
            new AppConfig(configKey:"cutOffDate",configValue:"04/26/2015 23:59:59").save(flush:true)
            params["cutOffDate"] = AppConfig.findByConfigKey("cutOffDate")?.configValue
    }
    
    void testNoJudgesList() {
        controller.list()
		
		assert Judge.count() == 0
		assert response.redirectedUrl == "/judge/create"
    }

	void test1JudgeList() {
		populateValidParams(params)
		Judge judge = new Judge(params)
		judge.metaClass.encodePassword = {'b'}
		judge.save(flush:true)
		SecUser su = new SecUser(params)
		su.metaClass.encodePassword = {'b'}
		su.save(flush:true)
		def model = controller.list()
		
		assert Judge.count() == 1
		assert model.judgeInstanceTotal == 1
	}

	void testMultipleJudgesList() {
		populateValidParams(params)
		Judge judge = new Judge(params)
		judge.metaClass.encodePassword = {'b'}
		judge.save(flush:true)
		params.username = "john"
		Judge judge2 = new Judge(params)
		judge2.metaClass.encodePassword = {'b'}
		judge2.save(flush:true)
		params.username = "eric"
		Judge judge3 = new Judge(params)
		judge3.metaClass.encodePassword = {'b'}
		judge3.save(flush:true)
		def model = controller.list()
		
		assert Judge.count() == 3
		assert model.judgeInstanceTotal == 3
	}
    void testEmptyCreate() {
        def model = controller.create()

		assert flash.message == null
        assert model.judgeInstance != null
    }
	
	void testPrefilledCreate() {
		populateValidParams(params)
		def model = controller.create()
		
		assert flash.message == null
		assert model.judgeInstance != null
		assert model.judgeInstance.username == 'peterj'
	}

    void testInvalidSave() {
        controller.save()

        assert model.judgeInstance != null
        assert view == '/judge/create'
    }

    void testValidSave() {
//        populateValidParams(params)
		params.username='peterj'
		params.password='a'
		params.firstName='PeterJ'
		params.lastName='Griffon'
		params.enabled=true
        controller.save()

        assert flash.message != null
        assert response.redirectedUrl == "/judge/show/1"
        assert Judge.count() == 1
		assert SecUserSecRole.count() == 1
    }

    void testInvalidShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/judge/list'
    }
	
	void testValidJudgeShow() {
		populateValidParams(params)
		def judge = new Judge(params)
		assert judge.save(flush:true) != null
		params.id = judge.id
		def model = controller.show()

		assert model.judgeInstance == judge
		assert Judge.count() == 1
	}

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/judge/list'
    }
	
	void testValidEdit() {
		populateValidParams(params)
		def judge = new Judge(params)
		assert judge.save(flush:true) != null
		params.id = judge.id
		def model = controller.edit()

		assert model.judgeInstance != null
		assert model.judgeInstance == judge
		assert model.judgeInstance.username == 'peterj'
	}

    void testInvalidUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/judge/list'
    }

    void testInvalidParamsInvalidUpdate() {
        populateValidParams(params)
        def judge = new Judge(params)
        assert judge.save(flush:true) != null
        params.id = judge.id
		params.username = ''
        controller.update()

        assert response.redirectedUrl == "/judge/edit/$params.id"
        assert flash.message != null
    }

    void testInvalidVersionInvalidUpdate() {
        populateValidParams(params)
		def judge = new Judge(params).save(flush:true)
        params.id = judge.id
        params.version = -1
        controller.update()

        assert response.redirectedUrl == "/judge/edit/$params.id"
        assert flash.message != null
    }

    void testValidParamsValidUpdate() {
        populateValidParams(params)
		def judge = new Judge(params)
		judge.metaClass.encodePassword= {'b'}
		judge.metaClass.isDirty = {false}
		judge.save(flush:true,failOnError:true)
		params.firstName = 'peat'
		params.id = judge.id
        controller.update()

        assert response.redirectedUrl == "/judge/show/$judge.id"
        assert flash.message != null
		assert judge.firstName == 'peat'
    }

    void testNoJudgeInvalidDelete() {
        controller.delete()
		
        assert flash.message != null
        assert response.redirectedUrl == '/judge/list'
    }

	void testJudgeValidDelete() {
        populateValidParams(params)
		SecRole role = new SecRole(params).save(flush:true,failOnError:true)
        def judge = new Judge(params)
		judge.metaClass.encodePassword = {'b'}
		judge.metaClass.isDirty = {false}
        assert judge.save(flush:true,failOnError:true) != null
		SecUserSecRole.create judge,role
        assert Judge.count() == 1
        params.id = judge.id
        controller.delete()

        assert Judge.count() == 0
        assert Judge.get(judge.id) == null
        assert response.redirectedUrl == '/judge/list'
    }
}