(ns ezlodge.ezlodge
  (:require [goog.net.XhrIo :as xhrio]
            [goog.json :as json])
  (:use [webfui.framework :only [launch-app]]
        [cljs.reader :only [read-string]]
        [webfui.utilities :only [get-attribute clicked]]))

(def uri "../../../sbemployee_ember/")

(defn render-all [state]
  (doseq [i state] 
    (let [{:keys [display_id name]} state]
       [:div [:p display_id]
             [:p  name]])))

(defn printever [event]
  (let [response (.-target event)]
    (let [rally (js->clj (.getResponseJson response) :keywordize-keys true)]
      (.write js/document (.getResponseJson response))
      (.write js/document rally)
      (.write js/document (first rally)))))

(defn getemps [event]
  (let [response (.-target event)]
    (js->clj (.getResponseJson response) :keywordize-keys true)))

(def my-state (atom (xhrio/send uri getemps "GET")))

(launch-app my-state render-all)
