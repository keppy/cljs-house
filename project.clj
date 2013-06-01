(defproject ezlodge "0.1.0-SNAPSHOT"
  :description "cljs-house | ClojureScript starting point"
  :url "http://jamesdominguez.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :source-paths ["src/clj"]
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [domina "1.0.2-SNAPSHOT"]
                 [hiccups "0.2.0"]
                 [shoreleave/shoreleave-remote "0.3.0"]
                 [enfocus "1.0.1"]]
  :plugins [[lein-cljsbuild "0.3.0"]
            [lein-tarsier "0.10.0"]]
  :cljsbuild {
              :builds [{
                      :source-paths ["src/cljs"]
                      :compiler {
                                 :output-to "resources/public/js/ezlodge.js"
                                 :optimizations :whitespace
                                 :pretty-print true}}]})
                      
