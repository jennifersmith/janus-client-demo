(defproject janus-client-demo "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-1909"]
                 [compojure "1.1.5"]
                 [hiccup "1.0.4"]
                 [hiccup-bootstrap "0.1.2"]
                 [domina "1.0.2"]
                 [com.cemerick/piggieback "0.1.0"]
                 [hiccups "0.2.0"]
                 [cljs-ajax "0.2.1"]
                 ]
  :source-paths ["src/clj"]

  :plugins [[lein-cljsbuild "0.3.2"]
            [lein-ring "0.8.7"]]

  :ring {:handler janus-client-demo.handler/app}

  :cljsbuild {:builds {:dev
                       {:source-paths ["src/cljs"]
                        :compiler {:output-to "resources/public/generated/dev.js"
                                   :optimizations :whitespace
                                   :pretty-print true}}
                       :prod
                       {:source-paths ["src/cljs"]
                        :compiler {:output-to "resources/public/generated/prod.js"
                                   :optimizations :advanced}}}}
  :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]
                 :init (use 'janus-client-demo.repl)})
