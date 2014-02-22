(ns mvMusic.handler
  (:use [compojure.core]
        [mvMusic.view.views]
        [mvMusic.global]
        [mvMusic.file-ops.dl])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))

(defroutes app-routes
  (GET "/" [] (main-page "/"))
  (GET [(str browse-path ":user-path") :user-path #".*"] 
       [user-path] (main-page user-path))
  (GET [(str download-path ":user-path") :user-path #".*"] 
       [user-path] (dl-file user-path))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
