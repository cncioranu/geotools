/**
 *
 * $Id$
 */
package net.opengis.ows20.validation;

import java.math.BigInteger;

import java.util.List;

/**
 * A sample validator interface for {@link net.opengis.ows20.BoundingBoxType}.
 * This doesn't really do anything, and it's not a real EMF artifact.
 * It was generated by the org.eclipse.emf.examples.generator.validator plug-in to illustrate how EMF's code generator can be extended.
 * This can be disabled with -vmargs -Dorg.eclipse.emf.examples.generator.validator=false.
 */
public interface BoundingBoxTypeValidator {
  boolean validate();

  boolean validateLowerCorner(List<Double> value);
  boolean validateUpperCorner(List<Double> value);
  boolean validateCrs(String value);
  boolean validateDimensions(BigInteger value);
}
