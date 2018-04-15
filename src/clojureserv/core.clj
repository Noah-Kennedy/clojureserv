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

(defn ^Socket mk-client-socket [^String address,
                                ^Integer port]
  (Socket.
    (InetAddress/getByName address)
    port))

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
  (send-off socket-agent #(do
                            (-> %
                                mk-tcp-writer
                                (.println str))
                            %)))

(defn print-with-socket-agent [socket-agent]
  (send-off socket-agent
            #(do
               (let [reader (mk-tcp-reader %)]
                 (while (.hasNextLine reader)
                   (println (.next reader)))
                 (println "empty"))
               %)))

(defn mk-server [^Integer port]
  (-> (ServerSocket. port)
      agent
      (send-off
        (fn [^ServerSocket server-socket]
          (.accept server-socket)))))

(defn mk-client [^String ipaddress
                 ^Integer port]
  (-> (mk-client-socket ipaddress port)
      agent))

(defmacro defserver [^Symbol name
                     ^Integer port]
  `(def ~name (mk-server ~port)))

(defmacro defclient [^Symbol name
                     ^String ipaddress
                     ^Integer port]
  `(def ~name (mk-client ~ipaddress ~port)))