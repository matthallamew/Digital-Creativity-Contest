package dcc

import org.junit.*
import grails.test.mixin.*

@TestFor(AppConfigController)
@Mock(AppConfig)
class AppConfigControllerTests {

    def populateValidParams(params) {
        assert params != null
        params.configKey = "testKey"
        params.configValue = "Test Value"
        params.remark = "This is only a test"
    }

    @Before
    void setUp() {
        controller.appConfigService = new AppConfigService()
    }

    void testIndex() {
        controller.index()
        assert "/appConfig/list" == response.redirectedUrl
    }

    void testList() {
        def model = controller.list()

        assert model.appConfigInstanceList.size() == 0
        assert model.appConfigInstanceTotal == 0
    }

    void test1ElementList() {
        populateValidParams(params)
        new AppConfig(params).save(flush:true)
        def model = controller.list()
        assert model.appConfigInstanceList.size() == 1
        assert model.appConfigInstanceTotal  == 1
        assert model.appConfigInstanceList[0].configKey == "testKey"
    }

    void test1ElementExtraParamsList() {
        populateValidParams(params)
        params.max2 = 10
        new AppConfig(params).save(flush:true)
        def model = controller.list()
        assert model.appConfigInstanceList.size() == 1
        assert model.appConfigInstanceTotal  == 1
        assert model.appConfigInstanceList[0].configKey == "testKey"
    }

    void testMoreThan1ElementList() {
        populateValidParams(params)
        new AppConfig(params).save(flush:true)
        new AppConfig(configKey:"anotherKey",configValue:"Test 2",remark:"Another test config value").save(flush:true)
        def model = controller.list()

        assert model.appConfigInstanceList.size() >= 2
        assert model.appConfigInstanceTotal  >=2
        assert model.appConfigInstanceList[1].configKey == "anotherKey"
    }

    void testEmptyCreate() {
        def model = controller.create()
        assert model.appConfigInstance != null
        assert model.appConfigInstance.count() == 0
    }

    void testPrefilledCreate() {
        populateValidParams(params)
        def model = controller.create()
        assert model.appConfigInstance != null
        assert model.appConfigInstance.configKey == "testKey"
    }

    void testNoValuesInvalidSave() {
        controller.save()

        assert model.appConfigInstance != null
        assert view == '/appConfig/create'
    }

    void testInvalidValueInvalidSave() {
        populateValidParams(params)
        params.configKey = ""
        controller.save()

        assert view == '/appConfig/create'
        assert model.appConfigInstance != null
        assert controller.flash.message != null
        assert AppConfig.count() == 0
    }

    void testValidSave() {
        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/appConfig/show/1'
        assert controller.flash.message != null
        assert AppConfig.count() == 1
    }

    void testInvalidShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/appConfig/list'
    }

    void testValidShow() {
        populateValidParams(params)
        def appConfig = new AppConfig(params)
        assert appConfig.save(flush:true) != null
        params.id = appConfig.id
        def model = controller.show()

        assert model.appConfigInstance == appConfig
        assert flash.message == null
        assert model.appConfigInstance.configKey == 'testKey'
    }

    void testNoIDInvalidEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/appConfig/list'
    }

    void testInvalidIDInvalidEdit() {
        populateValidParams(params)
        def appConfig = new AppConfig(params)
        assert appConfig.save(flush:true) != null
        params.id = 999
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/appConfig/list'
        assert params.id != appConfig.id
    }

    void testValidEdit() {
        populateValidParams(params)
        def appConfig = new AppConfig(params)
        assert appConfig.save(flush:true) != null
        params.id = appConfig.id
        def model = controller.edit()

        assert model.appConfigInstance == appConfig
    }

    void testInvalidUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/appConfig/list'
    }

    void testInvalidParamsUpdate() {
        populateValidParams(params)
        def appConfig = new AppConfig(params)
        assert appConfig.save(flush:true) != null
        params.id = appConfig.id
        params.configKey = ""
        controller.update()

        assert view == "/appConfig/edit"
        assert model.appConfigInstance != null
    }

    void testValidUpdate() {
        populateValidParams(params)
        def appConfig = new AppConfig(params)
        assert appConfig.save(flush:true) != null
        assert appConfig.configValue == "Test Value"
        params.configValue = "Updated Value"
        params.id = appConfig.id
        controller.update()

        assert response.redirectedUrl == "/appConfig/show/$appConfig.id"
        assert flash.message != null
        assert appConfig.configValue == "Updated Value"
    }

    void testInvalidDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/appConfig/list'
    }

    void testValidDelete() {
        populateValidParams(params)
        def appConfig = new AppConfig(params)
        assert appConfig.save(flush:true) != null
        assert AppConfig.count() == 1
        params.id = appConfig.id
        controller.delete()

        assert AppConfig.count() == 0
        assert AppConfig.get(appConfig.id) == null
        assert response.redirectedUrl == '/appConfig/list'
    }
}
