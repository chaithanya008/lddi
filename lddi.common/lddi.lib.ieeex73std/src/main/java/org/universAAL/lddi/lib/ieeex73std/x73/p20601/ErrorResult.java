/*
    Copyright 2007-2014 TSB, http://www.tsbtecnologias.es
    Technologies for Health and Well-being - Valencia, Spain

    See the NOTICE file distributed with this work for additional
    information regarding copyright ownership

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */
package org.universAAL.lddi.lib.ieeex73std.x73.p20601;
//

// This file was generated by the BinaryNotes compiler.
// See http://bnotes.sourceforge.net
// Any modifications to this file will be lost upon recompilation of the source ASN.1.
//

import org.universAAL.lddi.lib.ieeex73std.org.bn.*;
import org.universAAL.lddi.lib.ieeex73std.org.bn.annotations.*;
import org.universAAL.lddi.lib.ieeex73std.org.bn.annotations.constraints.*;
import org.universAAL.lddi.lib.ieeex73std.org.bn.coders.*;
import org.universAAL.lddi.lib.ieeex73std.org.bn.types.*;

@ASN1PreparedElement
@ASN1Sequence(name = "ErrorResult", isSet = false)
public class ErrorResult implements IASN1PreparedElement {

	@ASN1Element(name = "error-value", isOptional = false, hasTag = false, hasDefaultValue = false)

	private RoerErrorValue error_value = null;

	@ASN1Any(name = "")

	@ASN1Element(name = "parameter", isOptional = false, hasTag = false, hasDefaultValue = false)

	private byte[] parameter = null;

	public RoerErrorValue getError_value() {
		return this.error_value;
	}

	public void setError_value(RoerErrorValue value) {
		this.error_value = value;
	}

	public byte[] getParameter() {
		return this.parameter;
	}

	public void setParameter(byte[] value) {
		this.parameter = value;
	}

	public void initWithDefaults() {

	}

	private static IASN1PreparedElementData preparedData = CoderFactory.getInstance()
			.newPreparedElementData(ErrorResult.class);

	public IASN1PreparedElementData getPreparedData() {
		return preparedData;
	}

}
