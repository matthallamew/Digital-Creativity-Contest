package dcc



import org.junit.*
import grails.test.mixin.*

@TestFor(JudgeController)
@Mock(Judge)
class JudgeControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/judge/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.judgeInstanceList.size() == 0
        assert model.judgeInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.judgeInstance != null
    }

    void testSave() {
        controller.save()

        assert model.judgeInstance != null
        assert view == '/judge/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/judge/show/1'
        assert controller.flash.message != null
        assert Judge.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/judge/list'

        populateValidParams(params)
        def judge = new Judge(params)

        assert judge.save() != null

        params.id = judge.id

        def model = controller.show()

        assert model.judgeInstance == judge
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/judge/list'

        populateValidParams(params)
        def judge = new Judge(params)

        assert judge.save() != null

        params.id = judge.id

        def model = controller.edit()

        assert model.judgeInstance == judge
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/judge/list'

        response.reset()

        populateValidParams(params)
        def judge = new Judge(params)

        assert judge.save() != null

        // test invalid parameters in update
        params.id = judge.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/judge/edit"
        assert model.judgeInstance != null

        judge.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/judge/show/$judge.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        judge.clearErrors()

        populateValidParams(params)
        params.id = judge.id
        params.version = -1
        controller.update()

        assert view == "/judge/edit"
        assert model.judgeInstance != null
        assert model.judgeInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/judge/list'

        response.reset()

        populateValidParams(params)
        def judge = new Judge(params)

        assert judge.save() != null
        assert Judge.count() == 1

        params.id = judge.id

        controller.delete()

        assert Judge.count() == 0
        assert Judge.get(judge.id) == null
        assert response.redirectedUrl == '/judge/list'
    }
}
