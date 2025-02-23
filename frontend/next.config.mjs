import { PHASE_PRODUCTION_BUILD } from 'next/dist/shared/lib/constants.js';

/** @type {import('next').NextConfig} */
const nextConfig = phase => {
    const isBuild = phase === PHASE_PRODUCTION_BUILD;

    return {
        ...(isBuild && {
            output: 'export',
            distDir: '../backend/src/main/resources/static',
        }),
    }
};

export default nextConfig;
