package com.grailsinaction

class PostController {

    static scaffold = true
    def defaultAction = 'timeline'

    def timeline = {
        def user = User.findById(params.id) // Отыскать пользователя по параметру id
        [ user : user ]                     // Передать пользователя в представление
    }

    def addPost() {
        def user = User.findById(params.id) // Отыскивает пользователя по значению id
        if (user) {
            def post = new Post(params)     // Запись данных в новый объект Post
            user.addToPosts(post)           // Связывает новое сообщение с пользователем
            if (user.save()) {              // save() вернет false, если проверка в объекте Post окончится неудачей
                flash.message = "Successfully created Post!!! ${post.user.loginId}"
                post.save()
            } else {
                user.discard()
                flash.message = "Invalid or empty post"
            }
        } else {
            flash.message = "Invalid User Id"
        }
        redirect(action: 'timeline', id: params.id) // Возвращает пользователя к списку сообщений
    }
}
