(ns mvMusic.handler
  (:use 
        [mvMusic.view.views]
        [mvMusic.global])
  (:require [compojure.core :refer [GET defroutes]] 
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.json :as json]))

(defroutes app-routes
  (GET "/" [] (main-page))
  (GET [(str "/music/:user-path") :user-path #".*"] 
       [user-path] (main-page user-path))
  (GET "/widgets" [] {:body [{:name "Widget 1"} {:name "Widget 2"}]})
  #_(GET [(str "/download/:user-path") :user-path #".*"] 
       [user-path] (dl-file user-path))
  (GET "/json-test" [] {:body [55 {:var 56}]})
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (handler/site app-routes)
      (json/wrap-json-body)
      (json/wrap-json-response)))

