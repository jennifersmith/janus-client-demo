(ns janus-client-demo.repl
  (:require [domina :refer [log]] [clojure.browser.repl :as repl]))

(defn start-repl []
  (log "Cranking up the repl...")
  (repl/connect "http://localhost:9000/repl"))

(start-repl)

