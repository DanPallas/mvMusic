(ns mvMusic.global)

(def browse-path "/music/")

(def cfg-map {:music-folder (str (System/getProperty "user.home") "/" "Music")
              :temp-folder 
                (str (System/getProperty "user.home") "/" "Music/mvMusic-tmp")})
