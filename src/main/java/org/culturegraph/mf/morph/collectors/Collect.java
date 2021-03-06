/*
 *  Copyright 2013 Deutsche Nationalbibliothek
 *
 *  Licensed under the Apache License, Version 2.0 the "License";
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.culturegraph.mf.morph.collectors;

import org.culturegraph.mf.morph.FlushListener;
import org.culturegraph.mf.morph.Metamorph;
import org.culturegraph.mf.morph.NamedValuePipeHead;
import org.culturegraph.mf.morph.NamedValueSource;

/**
 * Base interface for all classes which act as collectors in {@link Metamorph}
 * 
 * @author Markus Michael Geipel
 *
 */
public interface Collect extends FlushListener, NamedValuePipeHead{

	void setWaitForFlush(final boolean waitForFlush);
	void setSameEntity(boolean sameEntity);
	void addNamedValueSource(final NamedValueSource namedValueSource);
	void setReset(boolean reset);

	String getName();
	void setName(String name);

	

}