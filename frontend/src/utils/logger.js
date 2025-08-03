const isDev = process.env.NODE_ENV !== "production";
const logLevel = process.env.REACT_APP_LOG_LEVEL || "debug";

export const logger = {
    debug: (...args) => {
        if (isDev && logLevel === 'debug') {
            console.debug('[DEBUG]:', ...args)
        }
    },

    info: (...args) => {
        if (isDev || logLevel === 'info' || logLevel === 'debug') {
            console.info('[INFO]:', ...args);
        }
    },

    warn: (...args) => {
        if (isDev || logLevel !== 'silent') {
            console.warn('[WARN]:', ...args);
        }
    },

    error: (...args) => {
        console.error('[ERROR]:', ...args);
    }
};