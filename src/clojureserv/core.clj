(ns clojureserv.core
  (:import (java.net Socket InetAddress)
           (java.io PrintWriter)
           (java.util Scanner)))

(defn ^Socket mk-tcp-socket [^String address,
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