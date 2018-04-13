(ns clojureserv.core
  (:import (java.net Socket InetAddress ServerSocket)
           (java.io PrintWriter)
           (java.util Scanner)))

(defn ^Socket mk-client-socket [^String address,
                             ^Integer port]
  (Socket. (InetAddress/getByName address) port))

(defn ^PrintWriter mk-tcp-writer [^Socket socket]
  (-> socket
      .getOutputStream
      PrintWriter.))

(defn ^Scanner mk-tcp-reader [^Socket socket]
  (-> socket
      .getInputStream
      Scanner.))

(defn write-with-socket-agent [socket-agent,
                               ^String str]
  (send-off socket-agent #(-> %
                              mk-tcp-writer
                              (.println str))))

(defn ^ServerSocket mk-server-socket [^Integer port]
  (ServerSocket. port))

(def server
  (-> (mk-server-socket 2401)
      agent))

(def client
  (-> (mk-client-socket "127.0.0.1" 2401)
      agent))