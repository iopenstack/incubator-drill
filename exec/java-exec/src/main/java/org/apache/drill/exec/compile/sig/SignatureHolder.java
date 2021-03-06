/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.drill.exec.compile.sig;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterators;

public class SignatureHolder implements Iterable<CodeGeneratorMethod>{
  static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(SignatureHolder.class);
  
  private final CodeGeneratorMethod[] methods;
  private final Map<String, Integer> methodMap;
  
  public SignatureHolder(Class<?> signature){
    Method[] reflectMethods = signature.getDeclaredMethods();
    Map<String, Integer> newMap = Maps.newHashMap(); 
    
    List<CodeGeneratorMethod> methodHolders = Lists.newArrayList();
    for(Method m : reflectMethods){
      if( (m.getModifiers() & Modifier.ABSTRACT) == 0) continue;
      methodHolders.add(new CodeGeneratorMethod(m));
    }
    
    methods = new CodeGeneratorMethod[methodHolders.size()];
    for(int i =0; i < methodHolders.size(); i++){
      methods[i] = methodHolders.get(i);
      newMap.put(methods[i].getMethodName(), i);
    }
    
    
    methodMap = ImmutableMap.copyOf(newMap);

    

  }
  
  public CodeGeneratorMethod get(int i){
    return methods[i];
  }

  @Override
  public Iterator<CodeGeneratorMethod> iterator() {
    return Iterators.forArray(methods);
  }
  
  public int size(){
    return methods.length;
  }
  
  public int get(String method){
    Integer meth =  methodMap.get(method);
    if(meth == null){
      throw new IllegalStateException(String.format("Unknown method requested of name %s.", method));
    }
    return meth;
  }

  @Override
  public String toString() {
    final int maxLen = 10;
    return "SignatureHolder [methods="
        + (methods != null ? Arrays.asList(methods).subList(0, Math.min(methods.length, maxLen)) : null) + "]";
  }
  
  
}
