/*
 * Copyright (c) 2006 wemove digital solutions. All rights reserved.
 */
package de.ingrid.iplug.management.usecase;

import de.ingrid.utils.IngridHit;
import de.ingrid.utils.query.IngridQuery;

/**
 * Interface for management iplug use cases.
 * 
 * @author joachim@wemove.com
 */
public interface ManagementUseCase {

    /**
     * Executes a use case of the management iplug.
     * 
     * @param query
     *            The query.
     * @param start
     *            The start of the result set to return.
     * @param length
     *            The length of the result set to return.
     * @param plugId
     *            The plugId of the management iplug.
     * @return The hits according toi the query.
     */
    public IngridHit[] execute(IngridQuery query, int start, int length, String plugId);

}
