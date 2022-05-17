DEFAULT_ENV="dev"
DEFAULT_LOG_PATH="/var/log"

_term() { 
  echo "Caught SIGTERM signal!" 
  kill "$child"
  wait "$child"
  echo "Killed process [$child]"
}

trap _term SIGTERM

if [ -z "${DEPLOY_ENV}" ]; then
  java $DEBUG_OPTION $JAVA_OPTS -jar -Dspring.profiles.active=dev $RUN_OPTIONS -DLOG_PATH=${DEFAULT_LOG_PATH} td-*.jar &
  child=$!
else
  DEFAULT_ENV="${DEPLOY_ENV}"
  java $DEBUG_OPTION $JAVA_OPTS -jar  -Dspring.profiles.active=${DEFAULT_ENV} $RUN_OPTIONS -DLOG_PATH=${DEFAULT_LOG_PATH} td-*.jar &
  child=$!
fi

wait "$child"
