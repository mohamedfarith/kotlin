package com.app.kotlin.practice.visibilitymodifiers


/*default visibility modifier is public for all classes
open - make the class available for inheritance
private - accessible only inside class
protected - accessible only by inherited classes and not available via instance
internal - available with in package for both instance and inherited classes
*/

class Bag : Book(){

    private fun takeBook(){
       page = "hello"

        var book: Book = Book()
        book.page = "hello"

    }
}