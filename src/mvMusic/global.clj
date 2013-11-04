(ns mvMusic.global)

(def browse-path "/music/")
(def download-path "/download/")

(def cfg-map {:music-folder (str (System/getProperty "user.home") "/" "Music")
              :temp-folder 
                (str (System/getProperty "user.home") "/" "mvMusic-tmp")})
