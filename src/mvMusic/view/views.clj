(ns mvMusic.view.views
  (:use [compojure.core]
        [hiccup core page]
        [mvMusic.file-ops.read])
  (:require [mvMusic.view.templates :as template]))

(defn main-page 
  [path]
  (let [folder (get-file path) ] 
    (html5
      (template/head (str "mvMusic /" (to-relative (str folder))))
      (template/body folder))))
