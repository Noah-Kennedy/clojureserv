(defproject clojureserv "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [venantius/pyro "0.1.2"]]
  :main ^:skip-aot clojureserv.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
