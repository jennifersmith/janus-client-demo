(ns janus-client-demo.repl
  (:require cljs.repl.browser))

(defn cljs []
   (cemerick.piggieback/cljs-repl
    :repl-env (cljs.repl.browser/repl-env :port 9000)))

