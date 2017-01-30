package com.lodoss.parser;

import java.util.ArrayList;

/**
 * Created by Boris on 30/01/17.
 */
public abstract class AbstractParser {

    /**
     * Returns reference to parent's {@link Node}
     * @param expression
     * @return
     */
    public abstract Node parse(String expression);

}
