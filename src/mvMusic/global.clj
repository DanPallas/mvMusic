(ns mvMusic.global)


;---------------Constants---------------------------
(def zip-buff-length 4194304)
(def db-name "library.db")
(def supported-files 
  #".*\.([mM][pP][34]|[mM]4[aA]|[oG][oG][gG]|[fF][lL][aA][cC]|[aA][aA][cC])$")
;---------------End Constants------------------------

(def cfg-map {:music-folder (str (System/getProperty "user.home") "/" "Music")
              :temp-folder 
                (str (System/getProperty "user.home") "/" ".mvMusic")})
(def exit-codes {:success 0, :db-failure -1})
