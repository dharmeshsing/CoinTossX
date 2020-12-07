environments {
    local {
        ext.envProp = 'local.properties'
        ext.softwarePath = '/Users/dharmeshsing/Documents/Masters/tmp'
    }

    windows {
        ext.envProp = 'windows.properties'
        ext.softwarePath = '/Users/dharmeshsing/Documents/cointoss_shared'
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
        ext.softwarePath = '/home/IvanJericevich/run/run'
    }
}