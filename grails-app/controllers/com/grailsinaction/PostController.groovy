package com.grailsinaction

class PostController {

    static scaffold = true

    def timeline = {
        def user = User.findByUserId(params.id) // Отыскать пользователя по параметру id
        [ user : user ]                         // Передать пользователя в представление
    }
}
