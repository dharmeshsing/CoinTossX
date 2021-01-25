environments {
    local {
        ext.envProp = 'local.properties'
        ext.softwarePath = '/home/ivanjericevich/run'
    }

    windows {
        ext.envProp = 'windows.properties'
        ext.softwarePath = '/home/ivanjericevich/run'
    }

    wits {
        ext.envProp = 'witsServer.properties'
        ext.softwarePath = '/hydrahome/1133465/Simulation'
    }

    chpc {
        ext.envProp = 'chpcServer.properties'
        ext.softwarePath = '/home/dsing/Simulation'
    }

    azure {
        ext.envProp = 'azure.properties'
        ext.softwarePath = '/home/ivanjericevich/run'
    }
}