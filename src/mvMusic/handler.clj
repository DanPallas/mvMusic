(ns mvMusic.handler
  (:use [compojure.core]
        [mvMusic.views]
        [mvMusic.global])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))


(defroutes app-routes
  (GET "/" [] (main-page "/"))
  (GET (str browse-path ":user-path") [user-path] (main-page user-path))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))

