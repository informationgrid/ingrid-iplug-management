package de.ingrid.iplug.management.webapp.object;

import org.springframework.stereotype.Service;

import de.ingrid.admin.object.AbstractDataType;

@Service
public class ManagementDataType extends AbstractDataType {

    public ManagementDataType() {
        super("management");
        setForceActive(true);
    }

}