package com.example.whynotdoctor

class GlobalData private constructor() {
    companion object {
        private var instance: GlobalData? = null

        fun getInstance(): GlobalData {
            if (instance == null) {
                instance = GlobalData()
            }
            return instance!!
        }
    }

    var _doctorid: String? = null
    var URL_G :String = "https://c9df-103-91-88-66.in.ngrok.io/"
}
