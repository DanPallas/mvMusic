(ns mvMusic.views
  (:use [compojure.core]
        [hiccup core page]
        [mvMusic.configuration]
        [mvMusic.file-ops])
  (:require [mvMusic.view-templates :as template]))

(defn main-page 
  [path]
  (let [c-path (clean path) ] 
    (html5
      (template/head (str "mvMusic /" (to-relative (str c-path))))
      (template/body c-path))))
