package dcc

import org.junit.*
import grails.test.mixin.*

@TestFor(RankController)
@Mock([Rank,Judge,Submission])
class RankControllerTests {
	
	@Before
	void setUp() {
		controller.rankService = new RankService()
		Judge.metaClass.encodePassword = {'a'}
	}

	def populateValidParams(params) {
		Judge judge = new Judge(username:'peterj',password:'a',firstName:'PeterJ',lastName:'Griffon',enabled:true)
		judge.metaClass.encodePassword = {'b'}
		judge.save(flush:true)
		Submission submission = new Submission(category:"Whatever",title:"My Subbmission",author:"Author",link:"http://youtu.be/5L28TM48bF0",emailAddr:"email1@domain.com").save(flush:true)
		assert params != null
		params["skill"] = 6
		params["creativity"] = 6
		params["aesthetic"] = 6
		params["purpose"] = 6
		params["total"] = 24
		params["judges"] = judge
		params["submissions"] = submission
	}
	
	void testIndex() {
		controller.index()

		assert response.redirectedUrl == "/"
		assert flash.message == "This method is inaccessible"
	}

	void testEmptyList() {
		def model = controller.list()

//		assert model.rankInstanceList.size() == 0
//		assert model.rankInstanceTotal == 0
		assert response.redirectedUrl == "/"
		assert flash.message == "This method is inaccessible"
	}

	void test1ElementList() {
		populateValidParams(params)
		Rank rank = new Rank(params).save(flush:true)

		def model = controller.list()

//		assert model.rankInstanceList.size() == 1
//		assert model.rankInstanceTotal == 1
//		assert model.rankInstanceList[0].skill == 6
		assert response.redirectedUrl == "/"
		assert flash.message == "This method is inaccessible"
	}

	void testMoreThan1ElementList() {
		populateValidParams(params)
		new Rank(params).save(flush:true)
		new Rank(params).save(flush:true)
		def model = controller.list()

//		assert model.rankInstanceList.size() >= 2
//		assert model.rankInstanceTotal >= 2
		assert response.redirectedUrl == "/"
		assert flash.message == "This method is inaccessible"
	}
	
	void testEmptyCreate() {
		def model = controller.create()

//		assert model == null
//		assert response.redirectedUrl == "/rank/list"
//		assert flash.message == "This method is inaccessible"
		assert response.redirectedUrl == "/"
		assert flash.message == "This method is inaccessible"
	}

	void testPrefilledParamsCreate() {
		populateValidParams(params)
		def model = controller.create()

//		assert model.rankInstance != null
//		assert controller.params.skill == 6
		assert response.redirectedUrl == "/"
		assert flash.message == "This method is inaccessible"
	}

	void testNoValuesInvalidSave() {
		controller.save()

//		assert model.rankInstance != null
//		assert view == '/rank/create'
		assert response.redirectedUrl == "/"
		assert flash.message == "This method is inaccessible"
	}
	
	void testInvalidValuesInvalidSave() {
		populateValidParams(params)
		params.judges = ""
		controller.save()

//		assert model.rankInstance != null
//		assert view == '/rank/create'
//		assert model.rankInstance.skill == 6
		assert response.redirectedUrl == "/"
		assert flash.message == "This method is inaccessible"
	}

	void testValidSave() {
		populateValidParams(params)
		controller.save()

//		assert response.redirectedUrl == '/rank/show/1'
//		assert controller.flash.message != null
//		assert Rank.count() == 1
		assert response.redirectedUrl == "/"
		assert flash.message == "This method is inaccessible"
	}

	void testInvalidShow() {
		controller.show()

		assert flash.message != null
		assert response.redirectedUrl == '/'
	}

	void testValidShow() {
		populateValidParams(params)
		Rank rank = new Rank(params)
		assert rank.save(flush:true) != null
		params.id = rank.id
		def model = controller.show()

		assert model.rankInstance == rank
		assert model.rankInstance.skill == 6
		assert flash.message == null
	}

	void testMultiRanksShow() {
		//fail "fix me"
		populateValidParams(params)
		Rank rank = new Rank(params)
		Rank rank2 = new Rank(params)
		assert rank.save(flush:true) != null
		assert rank2.save(flush:true) != null
		params.id = rank.id
		params.id = rank2.id
		def model = controller.show()

		assert model.rankInstance != rank
		assert model.rankInstance == rank2
		assert model.rankInstance.skill == 6
		assert flash.message == null
	}

	void testInvalidEdit() {
		controller.edit()

//		assert flash.message != null
//		assert response.redirectedUrl == '/rank/list'
		assert response.redirectedUrl == "/"
		assert flash.message == "This method is inaccessible"
	}

	void testValidEdit() {
		populateValidParams(params)
		Rank rank = new Rank(params)

		assert rank.save(flush:true) != null

		params.id = rank.id
		def model = controller.edit()
		
//		assert model.rankInstance == rank
//		assert model.rankInstance.skill == 6
//		assert flash.message == null
		assert response.redirectedUrl == "/"
		assert flash.message == "This method is inaccessible"
	}

	void testNullIDInvalidUpdate() {
		def model = controller.update()

//		assert flash.message != null
//		assert response.redirectedUrl == '/rank/list'
//		assert model == null
		assert response.redirectedUrl == "/"
		assert flash.message == "This method is inaccessible"
	}

	void testGoodIDInvalidUpdate() {
		params.id = 2
		def model = controller.update()

//		assert flash.message != null
//		assert response.redirectedUrl == '/rank/list'
//		assert model == null
		assert response.redirectedUrl == "/"
		assert flash.message == "This method is inaccessible"
	}
	
	void testInvalidParamsInvalidUpdate() {
		populateValidParams(params)
		Rank rank = new Rank(params)
		assert rank.save(flush:true) != null
		params.id = rank.id
		// test invalid parameters in update

		controller.update()

//		assert response.redirectedUrl == "/rank/edit/${params.id}"
//		assert flash.message != null
		assert response.redirectedUrl == "/"
		assert flash.message == "This method is inaccessible"
	}

	void testOutdatedVersionInvalidUpdate() {
		populateValidParams(params)
		Rank rank = new Rank(params)
		assert rank.save(flush:true) != null
		params.id = rank.id
		params.version = -1
		controller.update()
		
//		assert response.redirectedUrl == "/rank/edit/${params.id}"
//		assert flash.message != null
		assert response.redirectedUrl == "/"
		assert flash.message == "This method is inaccessible"
	}

	void testGoodParamsValidUpdate() {
		populateValidParams(params)
		Rank rank = new Rank(params)

		assert rank.save(flush:true) != null
		params.email='bob@domain.com'
		controller.update()

//		assert response.redirectedUrl == "/rank/list"
//		assert flash.message != null
		assert response.redirectedUrl == "/"
		assert flash.message == "This method is inaccessible"
	}

	void testUnchangedParamsValidUpdate() {
		populateValidParams(params)
		Rank rank = new Rank(params)

		assert rank.save(flush:true) != null
		controller.update()

//		assert response.redirectedUrl == "/rank/list"
//		assert flash.message != null
		assert response.redirectedUrl == "/"
		assert flash.message == "This method is inaccessible"
	}

	void testEmptyRankInvalidDelete() {
		controller.delete()
		
//		assert flash.message != null
//		assert response.redirectedUrl == '/rank/list'
		assert response.redirectedUrl == "/"
		assert flash.message == "This method is inaccessible"
	}

	void testGoodRankValidDelete() {
		populateValidParams(params)
		Rank rank = new Rank(params)

		assert rank.save() != null
		assert Rank.count() == 1

		params.id = rank.id
		controller.delete()

//		assert Rank.count() == 0
//		assert Rank.get(rank.id) == null
//		assert response.redirectedUrl == '/rank/list'
		assert response.redirectedUrl == "/"
		assert flash.message == "This method is inaccessible"
	}
}