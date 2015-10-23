package com.grailsinaction

class UserController {

    def scaffold = true

    static navigation = [
        [group:'tabs', action:'search', order: 90],
        [action: 'advSearch', title: 'Advanced Search', order: 95],
        [action: 'register', order: 99, isVisible: { true }]
    ]

    def stats = {
        User user = User.findByUserId(params.userId)
        if (user) {
            def sdf = new java.text.SimpleDateFormat('E') //Преобразует дату
            def postsOnDay = [:] //в день недели
            user.posts.each { post ->
                def dayOfWeek = sdf.format(post.dateCreated) //Преобразует дату
                if (postsOnDay[dayOfWeek]) {    //сообщения в день недели
                    postsOnDay[dayOfWeek]       //Отображает день недели и количество
                } else {                        //сообщений, отправленных в этот день
                    postsOnDay[dayOfWeek] = 1
                }
            }
            return [ userId: params.userId, postsOnDay: postsOnDay ]
        } else {
            flash.message = "No stats available for that user"
            redirect(uri: "/")
        }
    }
    def search = {

	}

	def results = {

		def users = User.findAllByUserIdLike("%${params.userId}%")
		return [ users: users, term : params.userId ]

	}

    def advSearch = {
        
    }

    def advResults = {

        def profileProps = Profile.metaClass.properties*.name
        def profiles = Profile.withCriteria {
                "${params.queryType}" {

                        params.each { field, value ->

                            if (profileProps.grep(field) && value) {
                                ilike(field, value)
                            }
                        }

                }

        }
        [ profiles : profiles ]

    }

    def register = {

        if (params) {
            def user = new User(params)
            if (user.validate()) {
                user.save()
                flash.message = "Successfully Create User"
                redirect(uri: '/')
            } else {
                flash.message = "Error Registering User"
                return [ user: user ]
            }
            
        }

    }

    def register2 = { UserRegistrationCommand urc ->
        if (urc.hasErrors()) {
            return [ user : urc ]
        } else {
            def user = new User(urc.properties)
            user.profile = new Profile(urc.properties)
            if (user.save()) {
                flash.message = "Welcome aboard, ${urc.fullName ?: urc.userId}"
                redirect(uri: '/')
            } else {
                // maybe not unique userId?
                return [ user : urc ]
            }
        }
    }

    def register3 = {
        // exercise for the reader
    }

    def profile = {
        
        if (params.id) {
            def user = User.findByUserId(params.id)
            if (user) {
                return [ profile : user.profile, userId : user.userId ]
            } else {
                response.sendError(404)
            }
        }

    }

}


class UserRegistrationCommand {

    String userId
    String password
    String passwordRepeat

    byte[] photo
    String fullName
    String bio
    String homepage
    String email
    String timezone
    String country
    String jabberAddress

    static constraints = {
        userId(size: 3..20)

        // Ensure password does not match userid
        password(size: 6..8, blank: false,
                 validator: { passwd, urc ->
                    return passwd != urc.userId
                })
        passwordRepeat(nullable: false,
                validator: { passwd2, urc ->
                    return passwd2 == urc.password
                })
        fullName(nullable: true)
        bio(nullable: true, maxSize: 1000)
        homepage(url: true, nullable: true)
        email(email: true, nullable: true)
        photo(nullable: true)
        country(nullable: true)
        timezone(nullable: true)
        jabberAddress(email: true, nullable: true)
    }


}

