(ns mvMusic.global)


;---------------Constants---------------------------
(def browse-path "/music/")
(def download-path "/download/")
(def zip-buff-length 4194304)
;---------------End Constants------------------------

(def cfg-map {:music-folder (str (System/getProperty "user.home") "/" "Music")
              :temp-folder 
                (str (System/getProperty "user.home") "/" "mvMusic-tmp/")})
