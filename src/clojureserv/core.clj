(ns clojureserv.core
  (:import (java.net Socket InetAddress ServerSocket)
           (java.io PrintWriter)
           (java.util Scanner)
           (clojure.lang Symbol)))

(require 'pyro.printer)
(pyro.printer/swap-stacktrace-engine!)
(set! *warn-on-reflection* true)
(def ^String localhost "127.0.0.1")
(def ^Integer test-port 2401)

(defn ^PrintWriter mk-tcp-writer [^Socket socket]
  (-> socket
      .getOutputStream
      (PrintWriter. true)))

(defn ^Scanner mk-tcp-reader [^Socket socket]
  (-> socket
      .getInputStream
      Scanner.))

(defn write-with-socket-agent [socket-agent,
                               ^String str]
  (send-off socket-agent
            #(do (-> % mk-tcp-writer (.println str)) %)))

(defn print-with-socket-agent [socket-agent]
  (send-off socket-agent
            ^Socket (fn [^Socket socket]
                      (do
                        (let [reader (mk-tcp-reader socket)]
                          (while (.hasNext reader)
                            (println (.next reader)))
                          (println "empty"))
                        %))))

(defmacro defserver [^Symbol name
                     ^Integer port]
  `(def ~name (-> (ServerSocket. ~port)
                  agent
                  (send-off
                    (fn [^ServerSocket ~'server-socket]
                      (.accept ~'server-socket))))))

(defmacro defclient [^Symbol name
                     ^String ipaddress
                     ^Integer port]
  `(def ~name (agent
                (Socket.
                  (InetAddress/getByName ~ipaddress)
                  ~port))))