package com.grailsinaction

class UserController {
    static scaffold = true
    def search() {

    }
    def results() { // Обрабатывает отправленную форму параметров
        //def users = User.findAllByLoginIdLike(params.loginId) // Поиск на основе формы
        return [
                //users: User.findAllByLoginIdLike("%${params.loginId}%"),
                //users: User.findAllByPassword('memory'),
                users: User.findAll(new User(password: 'memory')),
                totalUsers: User.count(),
                term : params.loginId ] // Передает представлению пользователей и критерий поиска
    }
}
